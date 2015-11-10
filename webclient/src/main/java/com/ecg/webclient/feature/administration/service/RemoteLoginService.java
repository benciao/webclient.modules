package com.ecg.webclient.feature.administration.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.authentication.PasswordEncoder;
import com.ecg.webclient.feature.administration.persistence.mapper.RemoteLoginMapper;
import com.ecg.webclient.feature.administration.persistence.modell.RemoteLogin;
import com.ecg.webclient.feature.administration.persistence.repo.RemoteLoginRepository;
import com.ecg.webclient.feature.administration.viewmodell.RemoteLoginDto;

/**
 * Service zum Bearbeiten von RemotLogins.
 * 
 * @author arndtmar
 */
@Component
public class RemoteLoginService
{
	static final Logger logger = LogManager.getLogger(RemoteLoginService.class.getName());

	private RemoteLoginRepository	remoteLoginRepository;
	private RemoteLoginMapper		remoteLoginMapper;

	@Autowired
	public RemoteLoginService(RemoteLoginRepository remoteLoginRepository, RemoteLoginMapper remoteLoginMapper)
	{
		this.remoteLoginRepository = remoteLoginRepository;
		this.remoteLoginMapper = remoteLoginMapper;
	}

	/**
	 * Löscht das RemoteLogin mit der angegebenen Id.
	 * 
	 * @param id
	 *            Id des RemoteLogins
	 */
	public void deleteForId(long id)
	{
		try
		{
			remoteLoginRepository.delete(id);
		}
		catch (final Exception e)
		{
			logger.error(e);
		}
	}

	/**
	 * Löscht alle zur Id des Fremdsystems gehörenden RemoteLogins.
	 * 
	 * @param remoteSystemId
	 *            id des Fremdsystems
	 */
	@Transactional
	public void deleteRemoteLoginsForRemoteSystemId(Long remoteSystemId)
	{
		try
		{
			remoteLoginRepository.deleteAllForRemoteSystemId(remoteSystemId);
		}
		catch (final Exception e)
		{
			logger.error(e);
		}
	}

	/**
	 * @param remoteSystemId
	 *            Fremdsystem Id
	 * @return Alle zur übergebenen Fremdsystem Id gehörenden RemoteLogins
	 */
	public List<RemoteLoginDto> findAllForRemoteSystemId(Long remoteSystemId)
	{
		List<RemoteLogin> attachedRemoteLogins = new ArrayList<RemoteLogin>();

		try
		{
			remoteLoginRepository.findAllForRemoteSystemId(remoteSystemId).forEach(e -> attachedRemoteLogins.add(e));
		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		AutoPopulatingList<RemoteLoginDto> result = new AutoPopulatingList<RemoteLoginDto>(RemoteLoginDto.class);

		for (RemoteLogin attachedRemoteLogin : attachedRemoteLogins)
		{
			result.add(remoteLoginMapper.mapToDto(attachedRemoteLogin));
		}

		return result;
	}

	/**
	 * @param userId
	 *            Id des Benutzers, für welchen die Fremdsystemanmeldungen
	 *            ermittelt werden sollen
	 * @return Eine Liste aller Fremdsystemanmeldungen für einen Benutzer
	 */
	public List<RemoteLoginDto> getAllRemoteLoginsForUserId(long userId)
	{
		List<RemoteLogin> attachedRemoteLogins = new ArrayList<RemoteLogin>();

		try
		{
			remoteLoginRepository.findAllForForUserId(userId).forEach(e -> attachedRemoteLogins.add(e));
		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		AutoPopulatingList<RemoteLoginDto> result = new AutoPopulatingList<RemoteLoginDto>(RemoteLoginDto.class);

		for (RemoteLogin attachedRemoteLogin : attachedRemoteLogins)
		{
			result.add(remoteLoginMapper.mapToDto(attachedRemoteLogin));
		}

		return result;
	}

	/**
	 * @param userId
	 *            Id des Benutzers, für welchen die Fremdsystemanmeldungen
	 *            ermittelt werden sollen
	 * @return Eine Liste aller aktiven Fremdsystemanmeldungen für einen
	 *         Benutzer
	 */
	public List<RemoteLoginDto> getEnabledRemoteLoginsForUserId(long userId)
	{
		List<RemoteLogin> attachedRemoteLogins = new ArrayList<RemoteLogin>();

		try
		{
			remoteLoginRepository.findEnabledForForUserId(userId).forEach(e -> attachedRemoteLogins.add(e));
		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		AutoPopulatingList<RemoteLoginDto> result = new AutoPopulatingList<RemoteLoginDto>(RemoteLoginDto.class);

		for (RemoteLogin attachedRemoteLogin : attachedRemoteLogins)
		{
			result.add(remoteLoginMapper.mapToDto(attachedRemoteLogin));
		}

		return result;
	}

	/**
	 * Speichert das zu übergebende RemoteLogin.
	 * 
	 * @param detachedRemoteLogin
	 *            das zu speichernde RemoteLogin
	 * @return Das gespeicherte RemoteLogin
	 */
	public RemoteLoginDto save(RemoteLoginDto detachedRemoteLogin)
	{
		try
		{
			RemoteLogin draftRemoteLogin = remoteLoginMapper.mapToEntity(detachedRemoteLogin);
			RemoteLogin persistedRemoteLogin = remoteLoginRepository.save(draftRemoteLogin);

			if (persistedRemoteLogin != null)
			{
				return remoteLoginMapper.mapToDto(persistedRemoteLogin);
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
	 * Aktualisiert die Einstellungen für eine Fremdsystemanmeldung.
	 * 
	 * @param dto
	 *            Fremdsystemanmeldung
	 */
	public void update(RemoteLoginDto dto)
	{
		try
		{
			remoteLoginRepository.updateForId(dto.getId(), dto.getRemoteSystemLoginName(),
					PasswordEncoder.encode2Way(dto.getRemoteSystemPassword()), dto.isEnabled());
		}
		catch (final Exception e)
		{
			logger.error(e);
		}
	}
}
