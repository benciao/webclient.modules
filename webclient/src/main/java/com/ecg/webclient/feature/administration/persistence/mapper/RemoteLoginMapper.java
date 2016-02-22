package com.ecg.webclient.feature.administration.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.authentication.PasswordEncoder;
import com.ecg.webclient.feature.administration.persistence.modell.RemoteLogin;
import com.ecg.webclient.feature.administration.persistence.repo.RemoteLoginRepository;
import com.ecg.webclient.feature.administration.persistence.repo.RemoteSystemRepository;
import com.ecg.webclient.feature.administration.persistence.repo.UserRepository;
import com.ecg.webclient.feature.administration.viewmodell.RemoteLoginDto;

/**
 * Mapped die Eigenschaften einer der Persistenz bekannten Entität auf ein
 * detachtes RemoteLogin oder umgekehrt.
 * 
 * @author arndtmar
 */
@Component
public class RemoteLoginMapper
{
	private RemoteLoginRepository	remoteLoginRepo;
	private RemoteSystemRepository	remoteSystemRepo;
	private UserRepository			userRepo;

	@Autowired
	public RemoteLoginMapper(RemoteLoginRepository remoteLoginRepo, RemoteSystemRepository remoteSystemRepo,
			UserRepository userRepo)
	{
		this.remoteLoginRepo = remoteLoginRepo;
		this.remoteSystemRepo = remoteSystemRepo;
		this.userRepo = userRepo;
	}

	/**
	 * Wandelt ein attachtes RemoteLogin in ein detachtes um.
	 * 
	 * @param remoteLogin
	 *            attachtes RemoteLogin
	 * @return Detachetes RemoteLogin
	 */
	public RemoteLoginDto mapToDto(RemoteLogin remoteLogin)
	{
		RemoteLoginDto dto = new RemoteLoginDto();
		dto.setId(remoteLogin.getId());
		dto.setDelete(false);
		dto.setEnabled(remoteLogin.isEnabled());
		dto.setRemoteSystemId(Long.toString(remoteLogin.getRemoteSystem().getId()));
		dto.setUserId(Long.toString(remoteLogin.getUser().getId()));
		dto.setRemoteSystemName(remoteLogin.getRemoteSystem().getName());
		dto.setRemoteSystemDescription(remoteLogin.getRemoteSystem().getDescription());
		dto.setRemoteSystemLoginName(remoteLogin.getRemoteSystemLoginName());
		if (remoteLogin.getRemoteSystemPassword() != null)
		{
			dto.setRemoteSystemPassword(PasswordEncoder.decode2Way(remoteLogin.getRemoteSystemPassword()));
		}

		return dto;
	}

	/**
	 * Wandelt eine Liste von attachten RemoteLogins in eine Liste von detachten
	 * RemoteLogins um.
	 * 
	 * @param remoteLogins
	 *            Liste von attachten RemoteLogins
	 * @return Liste von detachten RemoteLogins
	 */
	public List<RemoteLoginDto> mapToDtos(List<RemoteLogin> remoteLogins)
	{
		List<RemoteLoginDto> result = new AutoPopulatingList<RemoteLoginDto>(RemoteLoginDto.class);

		remoteLogins.forEach(e -> result.add(mapToDto(e)));

		return result;
	}

	/**
	 * Wandelt eine Liste von detachten RemoteLogins in eine Liste von
	 * RemoteLogins Gruppen um.
	 * 
	 * @param dtos
	 *            Liste von detachten RemoteLogins
	 * @return Liste von attachten RemoteLogins
	 */
	public List<RemoteLogin> mapToEntities(List<RemoteLoginDto> dtos)
	{
		List<RemoteLogin> result = new ArrayList<RemoteLogin>();

		dtos.forEach(e -> result.add(mapToEntity(e)));

		return result;
	}

	/**
	 * Wandelt ein detachtes RemoteLogin in eine attachtes um.
	 * 
	 * @param dto
	 *            Detachtes RemoteLogin
	 * @return Attachtes RemoteLogin
	 */
	public RemoteLogin mapToEntity(RemoteLoginDto dto)
	{
		RemoteLogin entity = new RemoteLogin();
		entity.setId(dto.getId());
		entity.setEnabled(dto.isEnabled());
		entity.setRemoteSystemLoginName(dto.getRemoteSystemLoginName());
		entity.setRemoteSystemPassword(PasswordEncoder.encode2Way(dto.getRemoteSystemPassword()));
		entity.setUser(userRepo.findOne(Long.parseLong(dto.getUserId())));
		entity.setRemoteSystem(remoteSystemRepo.findOne(Long.parseLong(dto.getRemoteSystemId())));

		RemoteLogin remotelogin = remoteLoginRepo.findOne(entity.getId());
		if (remotelogin != null)
		{
			return remotelogin.bind(entity);
		}

		return entity;
	}
}
