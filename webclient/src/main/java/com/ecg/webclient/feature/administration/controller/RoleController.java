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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecg.webclient.feature.administration.service.RoleService;
import com.ecg.webclient.feature.administration.setup.AdministrationFeature;
import com.ecg.webclient.feature.administration.setup.SecurityAdminAccessRole;
import com.ecg.webclient.feature.administration.setup.SetupSystemAccessRole;
import com.ecg.webclient.feature.administration.viewmodell.FeatureDto;
import com.ecg.webclient.feature.administration.viewmodell.FeatureRoleTreeGridDto;
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

	private RoleService roleService;

	@Autowired
	public RoleController(RoleService roleService)
	{
		this.roleService = roleService;
	}

	/**
	 * Behandelt POST-Requests vom Typ "/admin/userrole/save". Speichert
	 * Änderungen an Benutzerrollen.
	 * 
	 * @return Template
	 */
	@PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY + "') OR hasRole('"
			+ AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@Valid RoleConfig roleConfig, BindingResult bindingResult)
	{
		List<RoleDto> updateDtos = new ArrayList<RoleDto>();
		List<RoleDto> deleteDtos = new ArrayList<RoleDto>();

		for (FeatureRoleTreeGridDto dto : roleConfig.getFeatureRoleTreeGrid())
		{
			if (dto.getRole() != null)
			{
				if (dto.getRole().isDelete())
				{
					deleteDtos.add(dto.getRole());
				}
				else
				{
					updateDtos.add(dto.getRole());
				}
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
	 * Behandelt GET-Requests vom Typ "/admin/userrole". Lädt alle
	 * Benutzerrollen.
	 * 
	 * @return Template
	 */
	@PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY + "') OR hasRole('"
			+ AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
	@RequestMapping(method = RequestMethod.GET)
	public String showRoleConfig(Model model)
	{
		RoleConfig roleConfig = new RoleConfig();
		List<RoleDto> roles = roleService.getAllRoles(false);
		Collections.sort(roles, RoleDto.RoleDtoComparator);
		roleConfig.setFeatureRoleTreeGrid(generateTreeGrid());
		model.addAttribute("roleConfig", roleConfig);

		return getLoadingRedirectTemplate();
	}

	protected String getLoadingRedirectTemplate()
	{
		return "feature/administration/userrole";
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
