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

import com.ecg.webclient.feature.administration.service.LdapConfigService;
import com.ecg.webclient.feature.administration.setup.AdministrationFeature;
import com.ecg.webclient.feature.administration.setup.SecurityAdminAccessRole;
import com.ecg.webclient.feature.administration.setup.SetupSystemAccessRole;
import com.ecg.webclient.feature.administration.viewmodell.LdapConfigDto;

/**
 * Controller zur Bearbeitung von Requests aus Administrationsdialogen (Ldap).
 * 
 * @author arndtmar
 *
 */
@Scope("request")
@Controller
@RequestMapping(value = "/admin/ldap")
public class LdapController
{
    static final Logger logger = LogManager.getLogger(LdapController.class.getName());

    @Autowired
    private LdapConfigService ldapConfigService;

    /**
     * Behandelt POST-Requests vom Typ "/admin/ldap/save". Speichert Änderungen an der LDAP-konfiguration.
     * 
     * @return Template
     */
    @PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY
            + "') OR hasRole('" + AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveLdapConfig(@Valid LdapConfigDto ldapConfig, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            return getLoadingRedirectTemplate();
        }

        ldapConfigService.saveLdapConfig(ldapConfig);

        return "redirect:";
    }

    /**
     * Behandelt GET-Requests vom Typ "/admin/ldap". Lädt alle LDAP-konfigurationen.
     * 
     * @return Template
     */
    @PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY
            + "') OR hasRole('" + AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
    @RequestMapping(method = RequestMethod.GET)
    public String showLdapConfig(Model model)
    {
        LdapConfigDto ldapConfig = ldapConfigService.getLdapConfig();
        if (ldapConfig == null)
        {
            ldapConfig = new LdapConfigDto();
        }
        model.addAttribute("ldapConfigDto", ldapConfig);

        return getLoadingRedirectTemplate();
    }

    /**
     * Behandelt POST-Requests vom Typ "/admin/ldap/test". Testet die LDAP-konfiguration.
     * 
     * @return Template
     */
    @PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY
            + "') OR hasRole('" + AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String testLdapConfig(@Valid LdapConfigDto ldapConfig, BindingResult bindingResult)
    {
        if (ldapConfigService.isLdapConfigOk(ldapConfig))
        {
            ldapConfig.setConnectionSuccessful(true);
        }
        else
        {
            ldapConfig.setConnectionSuccessful(false);
        }
        return getLoadingRedirectTemplate();
    }

    protected String getLoadingRedirectTemplate()
    {
        return "feature/administration/ldap";
    }
}
