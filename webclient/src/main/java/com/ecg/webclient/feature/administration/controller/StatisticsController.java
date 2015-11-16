package com.ecg.webclient.feature.administration.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecg.webclient.feature.administration.setup.AdministrationFeature;
import com.ecg.webclient.feature.administration.setup.SecurityAdminAccessRole;
import com.ecg.webclient.feature.administration.setup.SetupSystemAccessRole;

/**
 * Controller zum Anzeigen von Statistiken.
 * 
 * @author arndtmar
 *
 */
@Scope("request")
@Controller
@RequestMapping(value = "/admin/statistic/global")
public class StatisticsController
{
	/**
	 * Behandelt GET-Requests vom Typ "/admin/statistic/global". Lädt alle
	 * Statistiken.
	 * 
	 * @return Template
	 */
	@PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY + "') OR hasRole('"
			+ AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
	@RequestMapping(method = RequestMethod.GET)
	public String showGlobalStatistics(Model model)
	{
		return getLoadingRedirectTemplate();
	}

	protected String getLoadingRedirectTemplate()
	{
		return "feature/administration/loginAttemptsGlobal";
	}
}
