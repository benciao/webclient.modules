package com.ecg.webclient.feature.administration.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ecg.webclient.feature.administration.authentication.AuthenticationUtil;
import com.ecg.webclient.feature.administration.service.ClientService;
import com.ecg.webclient.feature.administration.service.GroupService;
import com.ecg.webclient.feature.administration.service.RoleService;
import com.ecg.webclient.feature.administration.setup.AdministrationFeature;
import com.ecg.webclient.feature.administration.setup.SecurityAdminAccessRole;
import com.ecg.webclient.feature.administration.setup.SetupSystemAccessRole;
import com.ecg.webclient.feature.administration.viewmodell.ClientDto;
import com.ecg.webclient.feature.administration.viewmodell.FeatureDto;
import com.ecg.webclient.feature.administration.viewmodell.FeatureRoleTreeGridDto;
import com.ecg.webclient.feature.administration.viewmodell.GroupConfig;
import com.ecg.webclient.feature.administration.viewmodell.GroupDto;
import com.ecg.webclient.feature.administration.viewmodell.RoleDto;
import com.ecg.webclient.feature.administration.viewmodell.validator.GroupDtoValidator;

/**
 * Controller zur Bearbeitung von Requests aus Administrationsdialogen (Gruppen).
 * 
 * @author arndtmar
 *
 */
@Scope("request")
@Controller
@RequestMapping(value = "/admin/usergroup")
@SessionAttributes("groupConfig")
public class GroupController
{
    static final Logger        logger = LogManager.getLogger(GroupController.class.getName());

    @Autowired
    private GroupService       groupService;
    @Autowired
    ClientService              clientService;
    @Autowired
    private RoleService        roleService;
    @Autowired
    private AuthenticationUtil authUtil;
    @Autowired
    GroupDtoValidator          groupDtoValidator;

    /**
     * Behandelt POST-Requests vom Typ "/admin/usergroup/copy". Kopiert eine Benutzergruppe.
     * 
     * @return Template
     */
    @PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY
            + "') OR hasRole('" + AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    public String copyGroup(@Valid GroupConfig groupConfig, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            return getLoadingRedirectTemplate();
        }

        // Hack, da Client nicht direkt gesetzt werden kann
        ClientDto targetClient = clientService.getClient(groupConfig.getCopyGroup().getClient().getId());

        GroupDto copyGroup = groupConfig.getCopyGroup();
        copyGroup.setClient(targetClient);
        groupService.saveGroup(copyGroup);

        return "redirect:";
    }

    /**
     * Behandelt POST-Requests vom Typ "/admin/usergroup/save". Speichert Änderungen an Benutzergruppen.
     * 
     * @return Template
     */
    @PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY
            + "') OR hasRole('" + AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveGroup(@Valid GroupConfig groupConfig, BindingResult bindingResult)
    {
        List<GroupDto> updateDtos = new ArrayList<GroupDto>();
        List<GroupDto> deleteDtos = new ArrayList<GroupDto>();

        for (GroupDto dto : groupConfig.getGroups())
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

        groupService.deleteGroups(deleteDtos);

        groupConfig.removeDeleted();

        if (bindingResult.hasErrors())
        {
            return getLoadingRedirectTemplate();
        }

        groupService.saveGroups(updateDtos, authUtil);

        return "redirect:";
    }

    /**
     * Behandelt GET-Requests vom Typ "/admin/usergroup". Lädt alle zum aktuell ausgewählten Mandanten
     * gehörige Benutzergruppen und deren zugeordnete Benutzerrollen.
     * 
     * @return Template
     */
    @PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY
            + "') OR hasRole('" + AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
    @RequestMapping(method = RequestMethod.GET)
    public String showGroupConfig(Model model)
    {
        GroupConfig groupConfig = new GroupConfig();
        List<GroupDto> groups = groupService.getAllGroupsForClient(authUtil.getSelectedClient().getId());
        Collections.sort(groups, GroupDto.GroupDtoComparator);
        groupConfig.setGroups(groups);
        groupConfig.setFeatureRoleTreeGrid(generateTreeGrid());
        groupConfig.setClientId(authUtil.getSelectedClient().getId());
        groupConfig.setCopyGroup(new GroupDto());
        model.addAttribute("groupConfig", groupConfig);

        return getLoadingRedirectTemplate();
    }

    protected String getLoadingRedirectTemplate()
    {
        return "feature/administration/usergroup";
    }

    @InitBinder("groupConfig")
    protected void initGroupBinder(WebDataBinder binder)
    {
        binder.setValidator(groupDtoValidator);
    }

    private List<FeatureRoleTreeGridDto> generateTreeGrid()
    {
        List<FeatureRoleTreeGridDto> featureRoleTreeGrid = new ArrayList<FeatureRoleTreeGridDto>();

        TreeMap<FeatureDto, List<RoleDto>> featureRoleMap = new TreeMap<FeatureDto, List<RoleDto>>(
                FeatureDto.FeatureDtoComparator);

        for (RoleDto role : roleService.getAllRoles(false))
        {
            if (featureRoleMap.containsKey(role.getFeature()))
            {
                featureRoleMap.get(role.getFeature()).add(role);
            }
            else
            {
                List<RoleDto> list = new ArrayList<RoleDto>();
                list.add(role);
                featureRoleMap.put(role.getFeature(), list);
            }
        }

        for (List<RoleDto> roleList : featureRoleMap.values())
        {
            Collections.sort(roleList, RoleDto.RoleDtoComparator);
        }

        long parentRow = -1;
        long rowCounter = 0;
        for (FeatureDto feature : featureRoleMap.keySet())
        {
            FeatureRoleTreeGridDto featureRow = new FeatureRoleTreeGridDto();
            featureRow.setFeatureName(feature.getName());
            featureRow.setRowId(rowCounter);
            parentRow = rowCounter;
            rowCounter++;
            featureRoleTreeGrid.add(featureRow);

            for (RoleDto role : featureRoleMap.get(feature))
            {
                FeatureRoleTreeGridDto roleRow = new FeatureRoleTreeGridDto();
                roleRow.setRowId(rowCounter);
                rowCounter++;
                roleRow.setParentRowId(parentRow);
                roleRow.setRole(role);
                featureRoleTreeGrid.add(roleRow);
            }
        }

        return featureRoleTreeGrid;
    }
}
