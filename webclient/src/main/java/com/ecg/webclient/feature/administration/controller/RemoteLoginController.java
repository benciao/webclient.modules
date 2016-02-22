package com.ecg.webclient.feature.administration.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecg.webclient.feature.administration.authentication.AuthenticationUtil;
import com.ecg.webclient.feature.administration.service.RemoteLoginService;
import com.ecg.webclient.feature.administration.viewmodell.RemoteLoginConfig;
import com.ecg.webclient.feature.administration.viewmodell.RemoteLoginDto;

/**
 * Controller zur Bearbeitung von Requests aus Administrationsdialogen
 * (Fremdsystemanmeldung).
 * 
 * @author arndtmar
 *
 */
@Scope("request")
@Controller
@RequestMapping(value = "/admin/remotelogin")
public class RemoteLoginController
{
	static final Logger logger = LogManager.getLogger(RemoteLoginController.class.getName());

	private RemoteLoginService	remoteLoginService;
	private AuthenticationUtil	authUtil;

	@Autowired
	public RemoteLoginController(RemoteLoginService remoteLoginService, AuthenticationUtil authUtil)
	{
		this.remoteLoginService = remoteLoginService;
		this.authUtil = authUtil;
	}

	protected String getLoadingRedirectTemplate()
	{
		return "feature/administration/remotelogin";
	}

	/**
	 * Behandelt POST-Requests vom Typ "/admin/remotelogin/save". Speichert
	 * Änderungen an Fremdsystemanmeldungen.
	 * 
	 * @return Template
	 */

	@Transactional
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(RemoteLoginConfig remoteLoginConfig)
	{
		for (RemoteLoginDto dto : remoteLoginConfig.getRemoteLogins())
		{
			remoteLoginService.update(dto);
		}

		return "redirect:";
	}

	/**
	 * Behandelt GET-Requests vom Typ "/admin/remotelogin". Lädt alle
	 * Fremdsystemeanmeldungen.
	 * 
	 * @return Template
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String showRemoteSystemLoginConfig(Model model)
	{
		RemoteLoginConfig rlConfig = new RemoteLoginConfig();
		rlConfig.setRemoteLogins(remoteLoginService.getAllRemoteLoginsForUserId(authUtil.getCurrentUser().getId()));
		model.addAttribute("remoteLoginConfig", rlConfig);

		return getLoadingRedirectTemplate();
	}
}
