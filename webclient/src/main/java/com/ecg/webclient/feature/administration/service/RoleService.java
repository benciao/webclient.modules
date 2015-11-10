package com.ecg.webclient.feature.administration.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.autorisation.WebClientAccessRole;
import com.ecg.webclient.feature.administration.persistence.mapper.RoleMapper;
import com.ecg.webclient.feature.administration.persistence.modell.Role;
import com.ecg.webclient.feature.administration.persistence.repo.FeatureRepository;
import com.ecg.webclient.feature.administration.persistence.repo.RoleRepository;
import com.ecg.webclient.feature.administration.viewmodell.RoleDto;

/**
 * Service zum Bearbeiten von Benutzerrollen.
 * 
 * @author arndtmar
 */
@Component
public class RoleService
{
	static final Logger logger = LogManager.getLogger(RoleService.class.getName());

	RoleRepository				roleRepo;
	FeatureRepository			featureRepo;
	RoleMapper					roleMapper;
	List<WebClientAccessRole>	rolesToRegister;
	FeatureService				featureService;

	@Autowired
	public RoleService(RoleRepository roleRepo, RoleMapper roleMapper, List<WebClientAccessRole> rolesToRegister,
			FeatureRepository featureRepo, FeatureService featureService)
	{
		this.roleRepo = roleRepo;
		this.roleMapper = roleMapper;
		this.featureRepo = featureRepo;
		// stellt sicher, dass Features vor den Rollen registriert und
		// persistiert werden
		this.featureService = featureService;

		registerRoles(rolesToRegister);
	}

	/**
	 * Löscht die in der Liste enthaltenen Rollen.
	 * 
	 * @param detachedRoles
	 *            Liste mit zu löschenden Rollen
	 */
	public void deleteRoles(List<RoleDto> detachedRoles)
	{
		try
		{
			for (RoleDto detachedRole : detachedRoles)
			{
				Role persistentRole = roleRepo.findOne(detachedRole.getId());

				if (persistentRole != null)
				{
					roleRepo.delete(persistentRole);
				}
			}
		}
		catch (final Exception e)
		{
			logger.error(e);
		}
	}

	/**
	 * @param onlyEnabledRoles
	 *            true, wenn nur die aktiven Rollen geladen werden sollen, sonst
	 *            false
	 * @return Alle Rollen, wenn false, sonst nur die aktivierten Rollen
	 */
	public List<RoleDto> getAllRoles(boolean onlyEnabledRoles)
	{
		List<Role> attachedRoles = new ArrayList<Role>();

		try
		{
			if (!onlyEnabledRoles)
			{
				roleRepo.findAllRoles().forEach(e -> attachedRoles.add(e));
			}
			else
			{
				roleRepo.findAllEnabledRoles().forEach(e -> attachedRoles.add(e));
			}
		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		AutoPopulatingList<RoleDto> result = new AutoPopulatingList<RoleDto>(RoleDto.class);

		for (Role attachedRole : attachedRoles)
		{
			result.add(roleMapper.mapToDto(attachedRole));
		}

		return result;
	}

	/**
	 * @param roleIds
	 *            Liste mit IDs von Rollen
	 * @return Liste mit zu den IDs gehörenden Rollen, welche selbst aktiv sind
	 *         und dessen zugeordnetes Feature aktiv ist
	 */
	public List<RoleDto> getEnabledRolesWithEnabledFeatureForIds(List<Long> roleIds)
	{
		List<RoleDto> result = new ArrayList<RoleDto>();

		try
		{
			Iterable<Role> persistentRoles = roleRepo.findEnabledRolesWithEnabledFeatureForIds(roleIds);
			for (Role role : persistentRoles)
			{
				result.add(roleMapper.mapToDto(role));
			}

		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		return result;
	}

	/**
	 * @param roleIds
	 *            Liste mit IDs von Rollen
	 * @return Liste mit zu den IDs gehörenden Rollen
	 */
	public List<RoleDto> getRolesForIds(List<Long> roleIds)
	{
		List<RoleDto> result = new ArrayList<RoleDto>();

		try
		{
			Iterable<Role> persistentRoles = roleRepo.findAll(roleIds);
			for (Role role : persistentRoles)
			{
				result.add(roleMapper.mapToDto(role));
			}

		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		return result;
	}

	/**
	 * Speichert die zu übergebende Rolle.
	 * 
	 * @param detachedRole
	 *            die zu speichernde Rolle
	 * @return Die gespeicherte Rolle
	 */
	public RoleDto saveRole(RoleDto detachedRole)
	{
		try
		{
			Role draftRole = roleMapper.mapToEntity(detachedRole);
			Role persistedRole = roleRepo.save(draftRole);

			if (persistedRole != null)
			{
				return roleMapper.mapToDto(persistedRole);
			}
			else
			{
				return null;
			}
		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		return null;
	}

	/**
	 * Speichert die in der Liste enthaltenen Rollen.
	 * 
	 * @param detachedRoles
	 *            Liste mit zu speichernden Rollen
	 */
	public void saveRoles(List<RoleDto> detachedRoles)
	{
		try
		{
			detachedRoles.forEach(e -> saveRole(e));
		}
		catch (final Exception e)
		{
			logger.error(e);
		}
	}

	/**
	 * Registriert implementierte Rollen in der Persistenz.
	 * 
	 * @param rolesToRegister
	 *            Implementierte Rollen, welche persistiert werden müssen.
	 */
	@Transactional
	private void registerRoles(List<WebClientAccessRole> rolesToRegister)
	{
		this.rolesToRegister = rolesToRegister;
		for (WebClientAccessRole accessRole : rolesToRegister)
		{
			Role lookupRole = roleRepo.findRoleByNameAndFeature(accessRole.getRoleKey(),
					accessRole.getFeature().getFeatureKey());

			if (lookupRole == null)
			{
				Role newRole = new Role();
				newRole.setName(accessRole.getRoleKey());
				newRole.setFeature(featureRepo.findFeatureByName(accessRole.getFeature().getFeatureKey()));
				newRole.setEnabled(true);
				roleRepo.save(newRole);
			}
		}
	}
}
