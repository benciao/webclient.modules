package com.ecg.webclient.feature.administration.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecg.webclient.feature.administration.authentication.AuthenticationUtil;
import com.ecg.webclient.feature.administration.service.ClientService;
import com.ecg.webclient.feature.administration.service.EnvironmentService;
import com.ecg.webclient.feature.administration.service.FeatureService;
import com.ecg.webclient.feature.administration.service.GroupService;
import com.ecg.webclient.feature.administration.service.RoleService;
import com.ecg.webclient.feature.administration.service.UserService;
import com.ecg.webclient.feature.administration.viewmodell.ClientDto;
import com.ecg.webclient.feature.administration.viewmodell.EnvironmentDto;
import com.ecg.webclient.feature.administration.viewmodell.FeatureDto;
import com.ecg.webclient.feature.administration.viewmodell.GroupDto;
import com.ecg.webclient.feature.administration.viewmodell.RoleDto;
import com.ecg.webclient.feature.administration.viewmodell.UserDto;

/**
 * Controller zur Bearbeitung von Requests aus Administrationsdialogen.
 * 
 * @author arndtmar
 *
 */
@Scope("request")
@Controller
@RequestMapping(value = "/admin")
public class AdministrationController
{
	static final Logger	logger								= LogManager
			.getLogger(AdministrationController.class.getName());
	static final String	PROPERTY_NAME_SETUP_USER_PASSWORD	= "sec.setup.user.pw";

	@Autowired
	private AuthenticationUtil	authUtil;
	@Autowired
	private RoleService			roleService;
	@Autowired
	private FeatureService		featureService;
	@Autowired
	private EnvironmentService	environmentService;
	@Autowired
	private Environment			env;
	@Autowired
	private GroupService		groupService;
	@Autowired
	private ClientService		clientService;
	@Autowired
	private UserService			userService;

	/**
	 * Behandelt GET-Requests vom Typ "/admin".
	 * 
	 * @return Template
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String load()
	{
		return "feature/administration/administration";
	}

	/**
	 * Behandelt POST-Requests vom Typ "/admin". Aktualisiert serverseitig das
	 * ausgewählte Feature.
	 * 
	 * @return Template
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String load(@ModelAttribute("currentFeature") long id)
	{
		FeatureDto selectedFeature = featureService.getFeatureById(id);
		authUtil.setSelectedFeature(selectedFeature);
		
		return selectedFeature.getLoadingRedirectTemplate();
	}

	/**
	 * Behandelt POST-Requests vom Typ "/admin/setup/system". Initialisiert das
	 * System mit Standardmandant, Standardbenutzer usw.
	 * 
	 * @return Template
	 */
	@RequestMapping(value = "/setup/system", method = RequestMethod.GET)
	@Transactional
	public String setupSystem()
	{
		// Mandant erstellen
		ClientDto setupClient = new ClientDto();
		setupClient.setEnabled(true);
		setupClient.setName("SETUP_CLIENT");
		setupClient.setDescription("Client to setup the system");
		setupClient.setColor("#ff0000");

		ClientDto savedClient = clientService.getClientByName("SETUP_CLIENT");

		if (savedClient == null)
		{
			savedClient = clientService.saveClient(setupClient);
			logger.info("SETUP_CLIENT created");
		}

		// Benutzerrollen erstellen
		List<RoleDto> roles = roleService.getAllRoles(false);

		GroupDto savedGroup = groupService.getGroupByName("SETUP_GROUP");

		if (savedGroup == null)
		{
			GroupDto setupGroup = new GroupDto();
			setupGroup.setClient(savedClient);
			setupGroup.setName("SETUP_GROUP");
			setupGroup.setDescription("Group to setup system");
			setupGroup.setEnabled(true);
			savedGroup = setupGroup;
			logger.info("SETUP_GROUP created");
		}

		String roleIds = "";
		for (RoleDto role : roles)
		{
			if (roleIds.isEmpty())
			{
				roleIds = Long.toString(role.getId());
			}
			else
			{
				roleIds = roleIds + "," + Long.toString(role.getId());
			}
		}
		savedGroup.setRoleIds(roleIds);

		savedGroup = groupService.saveGroup(savedGroup, authUtil);

		String groupIds = "";
		groupIds = Long.toString(savedGroup.getId());

		UserDto setupUser = userService.getUserByLogin("setupuser");

		if (setupUser == null)
		{
			setupUser = new UserDto();
			setupUser.setLogin("setupuser");
			setupUser.setPassword(env.getRequiredProperty(PROPERTY_NAME_SETUP_USER_PASSWORD));
			setupUser.setFirstname("Setup");
			setupUser.setLastname("User");
			setupUser.setEnabled(true);
			setupUser.setChangePasswordOnNextLogin(false);

			setupUser.setEmail("setupuser@ecg-leipzig.de");
			setupUser.setInternal(true);
			setupUser.setGroupIds(groupIds);

			logger.info("Setup-User created");
		}

		setupUser.setDefaultClient(Long.toString(savedClient.getId()));
		userService.saveUser(setupUser);

		// Umgebung mit Eigenschaften erstellen
		EnvironmentDto detachedEnvironment = environmentService.getEnvironment();
		if (detachedEnvironment == null)
		{
			EnvironmentDto newEnvironment = new EnvironmentDto();
			newEnvironment.setPasswordChangeInterval(30);
			newEnvironment.setAllowedLoginAttempts(10);
			environmentService.saveEnvironment(newEnvironment);
		}

		return "login";
	}
}
