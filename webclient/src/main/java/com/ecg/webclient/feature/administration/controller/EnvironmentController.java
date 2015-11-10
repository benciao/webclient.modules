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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecg.webclient.feature.administration.service.EnvironmentService;
import com.ecg.webclient.feature.administration.setup.AdministrationFeature;
import com.ecg.webclient.feature.administration.setup.SecurityAdminAccessRole;
import com.ecg.webclient.feature.administration.setup.SetupSystemAccessRole;
import com.ecg.webclient.feature.administration.viewmodell.EnvironmentDto;
import com.ecg.webclient.feature.administration.viewmodell.validator.EnvironmentDtoValidator;

/**
 * Controller zur Bearbeitung von Requests aus Administrationsdialogen (Systemumgebung).
 * 
 * @author arndtmar
 *
 */
@Scope("request")
@Controller
@RequestMapping(value = "/admin/environment")
public class EnvironmentController
{
    static final Logger        logger = LogManager.getLogger(EnvironmentController.class.getName());

    @Autowired
    private EnvironmentService environmentService;
    @Autowired
    EnvironmentDtoValidator    environmentDtoValidator;

    /**
     * Behandelt POST-Requests vom Typ "/admin/environment/save". Speichert Änderungen an der Systemumgebung.
     * 
     * @return Template
     */
    @PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY
            + "') OR hasRole('" + AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveEnvironment(@Valid EnvironmentDto environment, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            return getLoadingRedirectTemplate();
        }

        environmentService.saveEnvironment(environment);

        return "redirect:";
    }

    /**
     * Behandelt GET-Requests vom Typ "/admin/environment". Lädt alle Umgebungseigenschaften.
     * 
     * @return Template
     */
    @PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY
            + "') OR hasRole('" + AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
    @RequestMapping(method = RequestMethod.GET)
    public String showEnvironment(Model model)
    {
        EnvironmentDto environment = environmentService.getEnvironment();
        model.addAttribute("environmentDto", environment);

        return getLoadingRedirectTemplate();
    }

    protected String getLoadingRedirectTemplate()
    {
        return "feature/administration/environment";
    }

    @InitBinder("environmentDto")
    protected void initEnvironmentBinder(WebDataBinder binder)
    {
        binder.setValidator(environmentDtoValidator);
    }
}
