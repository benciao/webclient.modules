package com.ecg.webclient.feature.administration.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecg.webclient.feature.administration.authentication.AuthenticationUtil;
import com.ecg.webclient.feature.administration.service.ClientService;
import com.ecg.webclient.feature.administration.service.GroupService;
import com.ecg.webclient.feature.administration.service.UserService;
import com.ecg.webclient.feature.administration.setup.AdministrationFeature;
import com.ecg.webclient.feature.administration.setup.SecurityAdminAccessRole;
import com.ecg.webclient.feature.administration.setup.SetupSystemAccessRole;
import com.ecg.webclient.feature.administration.viewmodell.ClientDto;
import com.ecg.webclient.feature.administration.viewmodell.GroupDto;
import com.ecg.webclient.feature.administration.viewmodell.UserConfig;
import com.ecg.webclient.feature.administration.viewmodell.UserDto;
import com.ecg.webclient.feature.administration.viewmodell.validator.UserDtoValidator;

/**
 * Controller zur Bearbeitung von Requests aus Administrationsdialogen
 * (Benutzer).
 * 
 * @author arndtmar
 *
 */
@Scope("request")
@Controller
@RequestMapping(value = "/admin/user")
public class UserController
{
	static final Logger logger = LogManager.getLogger(UserController.class.getName());

	private UserService			userService;
	private ClientService		clientService;
	private GroupService		groupService;
	private AuthenticationUtil	authUtil;
	private UserDtoValidator	userDtoValidator;

	@Autowired
	public UserController(UserService userService, ClientService clientService, GroupService groupService,
			AuthenticationUtil authUtil, UserDtoValidator userDtoValidator)
	{
		this.userService = userService;
		this.clientService = clientService;
		this.groupService = groupService;
		this.authUtil = authUtil;
		this.userDtoValidator = userDtoValidator;
	}

	@PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY + "') OR hasRole('"
			+ AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
	@RequestMapping(value = "/loginas/{userId}", method = RequestMethod.GET)
	public String loginAsUser(Model model, @PathVariable("userId") String userId)
	{
		authUtil.loginAsUser(Long.parseLong(userId));

		return "/main";
	}

	/**
	 * Behandelt POST-Requests vom Typ "/admin/user/save". Speichert Änderungen
	 * an Benutzern.
	 * 
	 * @return Template
	 */
	@PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY + "') OR hasRole('"
			+ AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveUser(@Valid UserConfig userConfig, BindingResult bindingResult)
	{
		List<UserDto> updateDtos = new ArrayList<UserDto>();
		List<UserDto> deleteDtos = new ArrayList<UserDto>();

		for (UserDto dto : userConfig.getUsers())
		{
			if (dto.isDelete())
			{
				deleteDtos.add(dto);
			}
			else
			{
				updateDtos.add(dto);
			}
		}

		userService.deleteUsers(deleteDtos);

		userConfig.removeDeleted();

		if (bindingResult.hasErrors())
		{
			return getLoadingRedirectTemplate();
		}

		userService.saveUsers(updateDtos);

		return "redirect:";
	}

	/**
	 * Behandelt einen Ajax Request zum Anzeigen von ausschließlich Mandanten,
	 * welchen die zugeordneten Gruppen angehören.
	 * 
	 * @return
	 */
	@PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY + "') OR hasRole('"
			+ AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
	@RequestMapping(value = "/availableClients/{groupIds}/{userId}", method = RequestMethod.GET)
	public String showAvailableClients(Model model, @PathVariable("groupIds") String groupIds,
			@PathVariable("userId") String userId)
	{
		List<Long> realGroupIds = getGroupIds(groupIds);
		List<ClientDto> clients = clientService.getAssignedClientsForGroups(realGroupIds);
		UserDto user = userService.getUserById(Long.parseLong(userId));

		model.addAttribute("availableClients", clients);

		if (user != null)
		{
			model.addAttribute("defaultClient", user.getDefaultClient());
		}
		else
		{
			model.addAttribute("defaultClient", "");
		}

		return getLoadingRedirectTemplate() + " :: availableClients";
	}

	/**
	 * Behandelt einen Ajax Request zum Anzeigen von zu einem Mandanten
	 * gehörende Gruppen.
	 * 
	 * @return
	 */
	@PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY + "') OR hasRole('"
			+ AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
	@RequestMapping(value = "/clientgroups/{clientId}", method = RequestMethod.GET)
	public String showClientGroups(Model model, @PathVariable("clientId") String clientId)
	{
		List<GroupDto> groups = groupService.getAllGroupsForClient(Long.parseLong(clientId));

		model.addAttribute("groups", groups);

		return "feature/administration/user :: clientGroups";
	}

	/**
	 * Behandelt GET-Requests vom Typ "/admin/user". Lädt alle Benutzer.
	 * 
	 * @return Template
	 */
	@PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY + "') OR hasRole('"
			+ AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
	@RequestMapping(method = RequestMethod.GET)
	public String showUserConfig(Model model)
	{
		UserConfig userConfig = new UserConfig();
		List<UserDto> users = userService.getAllUsers(false);
		Collections.sort(users, UserDto.UserDtoComparator);
		userConfig.setUsers(users);
		model.addAttribute("userConfig", userConfig);

		return getLoadingRedirectTemplate();
	}

	protected String getLoadingRedirectTemplate()
	{
		return "feature/administration/user";
	}

	@InitBinder("userConfig")
	protected void initUserBinder(WebDataBinder binder)
	{
		binder.setValidator(userDtoValidator);
	}

	private static List<Long> getGroupIds(String groupIds)
	{
		List<Long> result = new ArrayList<Long>();

		List<String> ids = Arrays.asList(groupIds.split(","));

		for (String id : ids)
		{
			result.add(Long.parseLong(id));
		}

		return result.size() != 0 ? result : null;
	}

}
