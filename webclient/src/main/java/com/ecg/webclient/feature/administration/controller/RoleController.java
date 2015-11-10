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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecg.webclient.feature.administration.service.RoleService;
import com.ecg.webclient.feature.administration.setup.AdministrationFeature;
import com.ecg.webclient.feature.administration.setup.SecurityAdminAccessRole;
import com.ecg.webclient.feature.administration.setup.SetupSystemAccessRole;
import com.ecg.webclient.feature.administration.viewmodell.RoleConfig;
import com.ecg.webclient.feature.administration.viewmodell.RoleDto;

/**
 * Controller zur Bearbeitung von Requests aus Administrationsdialogen (Rollen).
 * 
 * @author arndtmar
 *
 */
@Scope("request")
@Controller
@RequestMapping(value = "/admin/userrole")
public class RoleController
{
    static final Logger logger = LogManager.getLogger(RoleController.class.getName());

    @Autowired
    private RoleService roleService;

    /**
     * Behandelt POST-Requests vom Typ "/admin/userrole/save". Speichert Änderungen an Benutzerrollen.
     * 
     * @return Template
     */
    @PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY
            + "') OR hasRole('" + AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@Valid RoleConfig roleConfig, BindingResult bindingResult)
    {
        List<RoleDto> updateDtos = new ArrayList<RoleDto>();
        List<RoleDto> deleteDtos = new ArrayList<RoleDto>();

        for (RoleDto dto : roleConfig.getRoles())
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

        roleService.deleteRoles(deleteDtos);

        roleConfig.removeDeleted();

        if (bindingResult.hasErrors())
        {
            return getLoadingRedirectTemplate();
        }

        roleService.saveRoles(updateDtos);

        return "redirect:";
    }

    /**
     * Behandelt GET-Requests vom Typ "/admin/userrole". Lädt alle Benutzerrollen.
     * 
     * @return Template
     */
    @PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY
            + "') OR hasRole('" + AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
    @RequestMapping(method = RequestMethod.GET)
    public String showRoleConfig(Model model)
    {
        RoleConfig roleConfig = new RoleConfig();
        roleConfig.setRoles(roleService.getAllRoles(false));
        model.addAttribute("roleConfig", roleConfig);

        return getLoadingRedirectTemplate();
    }

    protected String getLoadingRedirectTemplate()
    {
        return "feature/administration/userrole";
    }
}
