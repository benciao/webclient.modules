package com.ecg.webclient.feature.administration.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.persistence.modell.RemoteSystem;
import com.ecg.webclient.feature.administration.persistence.repo.RemoteSystemRepository;
import com.ecg.webclient.feature.administration.service.RemoteLoginService;
import com.ecg.webclient.feature.administration.viewmodell.RemoteLoginDto;
import com.ecg.webclient.feature.administration.viewmodell.RemoteSystemDto;

/**
 * Mapped die Eigenschaften einer der Persistenz bekannten Entität auf ein
 * detachtes Fremdsystem oder umgekehrt.
 * 
 * @author arndtmar
 */
@Component
public class RemoteSystemMapper
{
	private RemoteSystemRepository	remoteSystemRepo;
	private RemoteLoginService		remoteLoginService;

	@Autowired
	public RemoteSystemMapper(RemoteSystemRepository remoteSystemRepo, RemoteLoginService remoteLoginService)
	{
		this.remoteSystemRepo = remoteSystemRepo;
		this.remoteLoginService = remoteLoginService;
	}

	/**
	 * Wandelt ein attachtes Fremdsystem in ein detachtes um.
	 * 
	 * @param remoteSystem
	 *            attachtes Fremdsystem
	 * @return Detachetes Fremdsystem
	 */
	public RemoteSystemDto mapToDto(RemoteSystem rm)
	{
		RemoteSystemDto dto = new RemoteSystemDto();
		dto.setDescription(rm.getDescription());
		dto.setName(rm.getName());
		dto.setEnabled(rm.isEnabled());
		dto.setDelete(false);
		dto.setId(rm.getId());
		dto.setLoginUrl(rm.getLoginUrl());
		dto.setLoginParameter(rm.getLoginParameter());
		dto.setPasswordParameter(rm.getPasswordParameter());
		dto.setLogoutUrl(rm.getLogoutUrl());
		dto.setAssignedUserIds(getAssignedUsers(rm.getId()));
		dto.setDoPostRequest(rm.isDoPostRequest());

		return dto;
	}

	/**
	 * Wandelt eine Liste von attachten Fremdsystemen in eine Liste von
	 * detachten Fremdsystemen um.
	 * 
	 * @param remoteSystems
	 *            Liste von attachten Fremdsystemen
	 * @return Liste von detachten Fremdsystemen
	 */
	public List<RemoteSystemDto> mapToDtos(List<RemoteSystem> remoteSystems)
	{
		List<RemoteSystemDto> result = new AutoPopulatingList<RemoteSystemDto>(RemoteSystemDto.class);

		remoteSystems.forEach(e -> result.add(mapToDto(e)));

		return result;
	}

	/**
	 * Wandelt eine Liste von detachten Fremdsystemen in eine Liste von
	 * Fremdsystemen Gruppen um.
	 * 
	 * @param dtos
	 *            Liste von detachten Fremdsystemen
	 * @return Liste von attachten Fremdsystemen
	 */
	public List<RemoteSystem> mapToEntities(List<RemoteSystemDto> dtos)
	{
		List<RemoteSystem> result = new ArrayList<RemoteSystem>();

		dtos.forEach(e -> result.add(mapToEntity(e)));

		return result;
	}

	/**
	 * Wandelt ein detachtes Fremdsystem in eine attachtes um.
	 * 
	 * @param dto
	 *            Detachtes Fremdsystem
	 * @return Attachtes Fremdsystem
	 */
	public RemoteSystem mapToEntity(RemoteSystemDto dto)
	{
		RemoteSystem entity = new RemoteSystem();
		entity.setId(dto.getId());
		entity.setDescription(dto.getDescription());
		entity.setName(dto.getName());
		entity.setEnabled(dto.isEnabled());
		entity.setLoginUrl(dto.getLoginUrl());
		entity.setLoginParameter(dto.getLoginParameter());
		entity.setPasswordParameter(dto.getPasswordParameter());
		entity.setLogoutUrl(dto.getLogoutUrl());
		entity.setDoPostRequest(dto.isDoPostRequest());

		RemoteSystem remoteSystem = remoteSystemRepo.findOne(entity.getId());
		if (remoteSystem != null)
		{
			return remoteSystem.bind(entity);
		}

		return entity;
	}

	private String getAssignedUsers(long remoteSystemId)
	{
		String assignedUserIds = "";
		List<RemoteLoginDto> assignedRemoteLogins = remoteLoginService.findAllForRemoteSystemId(remoteSystemId);

		for (RemoteLoginDto assignedRemoteLogin : assignedRemoteLogins)
		{
			if (assignedUserIds.isEmpty() || assignedUserIds.length() == 0)
			{
				assignedUserIds = assignedRemoteLogin.getUserId();
			}
			else
			{
				assignedUserIds = assignedUserIds + "," + assignedRemoteLogin.getUserId();
			}
		}

		return assignedUserIds;
	}
}
