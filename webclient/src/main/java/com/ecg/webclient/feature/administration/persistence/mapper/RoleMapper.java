package com.ecg.webclient.feature.administration.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.persistence.modell.Role;
import com.ecg.webclient.feature.administration.persistence.repo.RoleRepository;
import com.ecg.webclient.feature.administration.viewmodell.RoleDto;

/**
 * Mapped die Eigenschaften einer der Parsistenz bekannten Entität auf eine
 * detachted Rolle oder umgekehrt.
 * 
 * @author arndtmar
 */
@Component
public class RoleMapper
{
	private RoleRepository	roleRepo;
	private FeatureMapper	featureMapper;

	@Autowired
	public RoleMapper(RoleRepository roleRepo, FeatureMapper featureMapper)
	{
		this.roleRepo = roleRepo;
		this.featureMapper = featureMapper;
	}

	/**
	 * Wandelt eine attachte Rolle in eine detachte um.
	 * 
	 * @param role
	 *            attachte Rolle
	 * @return Detachete Rolle
	 */
	public RoleDto mapToDto(Role attachedRole)
	{
		RoleDto detachedRole = new RoleDto();
		detachedRole.setName(attachedRole.getName());
		detachedRole.setEnabled(attachedRole.isEnabled());
		detachedRole.setDelete(false);
		detachedRole.setId(attachedRole.getId());
		detachedRole.setFeature(featureMapper.mapToDto(attachedRole.getFeature()));

		return detachedRole;
	}

	/**
	 * Wandelt eine Liste von attacheten Rollen in eine Liste von detachten
	 * Rollen um.
	 * 
	 * @param roles
	 *            Liste von attacheten Rollen
	 * @return Liste von detachten Rollen
	 */
	public List<RoleDto> mapToDtos(List<Role> attachedRoles)
	{
		List<RoleDto> result = new AutoPopulatingList<RoleDto>(RoleDto.class);

		attachedRoles.forEach(e -> result.add(mapToDto(e)));

		return result;
	}

	/**
	 * Wandelt eine Liste von detachten Rollen in eine Liste von attachten
	 * Rollen um.
	 * 
	 * @param dtos
	 *            Liste von detachten Rollen
	 * @return Liste von zum Speichern vorbereiteten Rollen
	 */
	public List<Role> mapToEntities(List<RoleDto> detachedRoles)
	{
		List<Role> result = new ArrayList<Role>();

		detachedRoles.forEach(e -> result.add(mapToEntity(e)));

		return result;
	}

	/**
	 * Wandelt eine detachte Rolle in eine attachte um.
	 * 
	 * @param dto
	 *            Detachte Rolle
	 * @return zum Speichern vorbereitete Rolle
	 */
	public Role mapToEntity(RoleDto detachedRole)
	{
		Role entity = new Role();
		entity.setId(detachedRole.getId());
		entity.setName(detachedRole.getName());
		entity.setEnabled(detachedRole.isEnabled());
		entity.setFeature(featureMapper.mapToEntity(detachedRole.getFeature()));

		Role persistentRole = roleRepo.findOne(entity.getId());
		if (persistentRole != null)
		{
			return persistentRole.bind(entity);
		}

		return entity;
	}
}
