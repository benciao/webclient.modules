package com.ecg.webclient.feature.administration.controller;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecg.webclient.feature.administration.authentication.AuthenticationUtil;
import com.ecg.webclient.feature.administration.service.FeatureService;
import com.ecg.webclient.feature.administration.setup.AdministrationFeature;
import com.ecg.webclient.feature.administration.setup.SecurityAdminAccessRole;
import com.ecg.webclient.feature.administration.setup.SetupSystemAccessRole;
import com.ecg.webclient.feature.administration.viewmodell.FeatureConfig;

/**
 * Controller zur Bearbeitung von Requests aus Administrationsdialogen
 * (Feature).
 * 
 * @author arndtmar
 *
 */
@Scope("request")
@Controller
@RequestMapping(value = "/admin/feature")
public class FeatureController
{
	static final Logger logger = LogManager.getLogger(FeatureController.class.getName());

	@Autowired
	private FeatureService		featureService;
	@Autowired
	private AuthenticationUtil	authUtil;

	/**
	 * Behandelt POST-Requests vom Typ "/admin/feature/save". Speichert
	 * Änderungen an Feature-Konfiguration.
	 * 
	 * @return Template
	 */
	@PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY + "') OR hasRole('"
			+ AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveFeatures(@Valid FeatureConfig featureConfig, BindingResult bindingResult)
	{
		if (bindingResult.hasErrors())
		{
			return getLoadingRedirectTemplate();
		}

		featureService.saveFeatures(featureConfig.getFeatures());
		authUtil.setNewAuthority();

		return "redirect:";
	}

	/**
	 * Behandelt GET-Requests vom Typ "/admin/feature". Lädt alle Features.
	 * 
	 * @return Template
	 */
	@PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY + "') OR hasRole('"
			+ AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
	@RequestMapping(method = RequestMethod.GET)
	public String showFeatureConfig(Model model)
	{
		FeatureConfig featureConfig = new FeatureConfig();
		featureConfig.setFeatures(featureService.getAllFeatures(false, false));
		model.addAttribute("featureConfig", featureConfig);

		return getLoadingRedirectTemplate();
	}

	protected String getLoadingRedirectTemplate()
	{
		return "feature/administration/feature";
	}
}
