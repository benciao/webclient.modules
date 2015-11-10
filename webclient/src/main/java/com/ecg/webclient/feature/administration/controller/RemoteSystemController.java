package com.ecg.webclient.feature.administration.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ecg.webclient.feature.administration.service.RemoteSystemService;
import com.ecg.webclient.feature.administration.service.UserService;
import com.ecg.webclient.feature.administration.setup.AdministrationFeature;
import com.ecg.webclient.feature.administration.setup.SecurityAdminAccessRole;
import com.ecg.webclient.feature.administration.setup.SetupSystemAccessRole;
import com.ecg.webclient.feature.administration.viewmodell.RemoteSystemConfig;
import com.ecg.webclient.feature.administration.viewmodell.RemoteSystemDto;
import com.ecg.webclient.feature.administration.viewmodell.validator.RemoteSystemDtoValidator;

/**
 * Controller zur Bearbeitung von Requests aus Administrationsdialogen
 * (Fremdsystem).
 * 
 * @author arndtmar
 *
 */
@Scope("request")
@Controller
@RequestMapping(value = "/admin/remotesystem")
public class RemoteSystemController
{
	static final Logger			logger	= LogManager.getLogger(RemoteSystemController.class.getName());
	@Autowired
	RemoteSystemService			remoteSystemService;
	@Autowired
	RemoteSystemDtoValidator	remoteSystemDtoValidator;
	@Autowired
	UserService					userService;

	/**
	 * Behandelt POST-Requests vom Typ "/admin/remotesystem/save". Speichert
	 * Änderungen an Fremdsystemen.
	 * 
	 * @return Template
	 */
	@PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY + "') OR hasRole('"
			+ AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@Valid RemoteSystemConfig remoteSystemConfig, BindingResult bindingResult)
	{
		List<RemoteSystemDto> updateDtos = new ArrayList<RemoteSystemDto>();
		List<RemoteSystemDto> deleteDtos = new ArrayList<RemoteSystemDto>();

		for (RemoteSystemDto dto : remoteSystemConfig.getRemoteSystems())
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

		remoteSystemService.deleteRemoteSystems(deleteDtos);

		remoteSystemConfig.removeDeleted();

		if (bindingResult.hasErrors())
		{
			return getLoadingRedirectTemplate();
		}

		remoteSystemService.saveRemoteSystems(updateDtos);

		return "redirect:";
	}

	/**
	 * Behandelt POST-Requests (Ajax) vom Typ "/admin/remotesystem/test". Testet
	 * die Verbindung zum Fremdsystemen.
	 * 
	 * @return Template
	 */
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody String test(@RequestParam String url)
	{
		Boolean result = remoteSystemService.testConnection(url);
		return result.toString();
	}

	/**
	 * Behandelt GET-Requests vom Typ "/admin/remotesystem". Lädt alle
	 * Fremdsysteme.
	 * 
	 * @return Template
	 */
	@PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY + "') OR hasRole('"
			+ AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
	@RequestMapping(method = RequestMethod.GET)
	public String showRemoteSystemConfig(Model model)
	{
		RemoteSystemConfig rmConfig = new RemoteSystemConfig();
		rmConfig.setRemoteSystems(remoteSystemService.getAllRemoteSystems(false));
		rmConfig.setAvailableUsers(userService.getAllUsers(false));
		model.addAttribute("remoteSystemConfig", rmConfig);

		return getLoadingRedirectTemplate();
	}

	protected String getLoadingRedirectTemplate()
	{
		return "feature/administration/remotesystem";
	}

	@InitBinder("remoteSystemConfig")
	protected void initRemoteSystemBinder(WebDataBinder binder)
	{
		binder.setValidator(remoteSystemDtoValidator);
	}
}
