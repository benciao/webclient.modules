package com.ecg.webclient.feature.administration.service;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.persistence.mapper.RemoteSystemMapper;
import com.ecg.webclient.feature.administration.persistence.modell.RemoteSystem;
import com.ecg.webclient.feature.administration.persistence.repo.RemoteSystemRepository;
import com.ecg.webclient.feature.administration.viewmodell.RemoteLoginDto;
import com.ecg.webclient.feature.administration.viewmodell.RemoteSystemDto;

/**
 * Service zum Bearbeiten von Fremdsystemen.
 * 
 * @author arndtmar
 */
@Component
public class RemoteSystemService
{
	static final Logger				logger	= LogManager.getLogger(RemoteSystemService.class.getName());
	private RemoteSystemRepository	remoteSystemRepo;
	private RemoteLoginService		remoteLoginService;
	private RemoteSystemMapper		remoteSystemMapper;

	@Autowired
	public RemoteSystemService(RemoteSystemRepository remoteSystemRepo, RemoteSystemMapper remoteSystemMapper,
			RemoteLoginService remoteLoginService)
	{
		this.remoteSystemRepo = remoteSystemRepo;
		this.remoteSystemMapper = remoteSystemMapper;
		this.remoteLoginService = remoteLoginService;
	}

	/**
	 * Löscht die in der Liste enthaltenen Fremdsysteme.
	 * 
	 * @param detachedRemoteSystems
	 *            Liste mit zu löschenden Fremdsystemen
	 */
	public void deleteRemoteSystems(List<RemoteSystemDto> detachedRemoteSystems)
	{
		try
		{
			for (RemoteSystemDto detachedRemoteSystem : detachedRemoteSystems)
			{
				RemoteSystem persistentRemoteSystem = remoteSystemRepo.findOne(detachedRemoteSystem.getId());

				if (persistentRemoteSystem != null)
				{
					remoteLoginService.deleteRemoteLoginsForRemoteSystemId(persistentRemoteSystem.getId());
					remoteSystemRepo.delete(persistentRemoteSystem);
				}
			}
		}
		catch (final Exception e)
		{
			logger.error(e);
		}
	}

	/**
	 * Testet die Verbindung zum Fremdsystem.
	 * 
	 * @param url
	 *            URL des Fremdsystems.
	 * @return true, wenn Verbindung erfolgreich, sonst false.
	 */
	public boolean testConnection(final String url)
	{
		try
		{
			CloseableHttpClient client = null;
			CloseableHttpResponse response = null;

			try
			{
				client = HttpClients.createDefault();

				HttpGet request = new HttpGet(url);
				response = client.execute(request);
				return response.getStatusLine().getStatusCode() == 200 ? true : false;
			}
			catch (Exception ex)
			{
				return false;
			}
			finally
			{
				if (response != null)
				{
					response.close();
				}
			}
		}
		catch (Exception x)
		{
			return false;
		}
	}

	/**
	 * Meldet den zur übergebenen Id gehörenden Benutzer an die für ihn
	 * konfigurierten Fremdsysteme an.
	 * 
	 * @param userId
	 *            Id des Nutzers, für welchen die Anmeldung an Fremdsystemen
	 *            durchgeführt werden soll.
	 * @return Liste mit den generierten Cookies
	 */
	public List<Cookie> doRemoteLogin(final long userId)
	{
		List<Cookie> result = new ArrayList<Cookie>();

		try
		{
			CookieHandler.setDefault(new CookieManager());

			List<RemoteLoginDto> enabledRemoteLogins = remoteLoginService.getEnabledRemoteLoginsForUserId(userId);

			for (RemoteLoginDto enabledRemoteLogin : enabledRemoteLogins)
			{
				final String login = enabledRemoteLogin.getRemoteSystemLoginName();
				final String password = enabledRemoteLogin.getRemoteSystemPassword();

				RemoteSystemDto remoteSystem = getRemoteSystemById(
						Long.parseLong(enabledRemoteLogin.getRemoteSystemId()));

				if (remoteSystem.isEnabled())
				{
					CloseableHttpClient client = null;
					CloseableHttpResponse response = null;

					List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
					urlParameters.add(new BasicNameValuePair(remoteSystem.getLoginParameter(), login));
					urlParameters.add(new BasicNameValuePair(remoteSystem.getPasswordParameter(), password));

					try
					{
						client = HttpClients.createDefault();
						HttpClientContext context = HttpClientContext.create();

						if (remoteSystem.isDoPostRequest())
						{
							HttpPost request = new HttpPost(remoteSystem.getLoginUrl());
							request.setEntity(new UrlEncodedFormEntity(urlParameters));
							response = client.execute(request, context);
						}
						else
						{
							String url = remoteSystem.getLoginUrl();

							for (NameValuePair pair : urlParameters)
							{
								if (!url.contains("?"))
								{
									url = url + "?";
								}
								else
								{
									url = url + "&";
								}
								url = url + pair.getName() + "=" + pair.getValue();
							}

							HttpGet request = new HttpGet(url);
							response = client.execute(request, context);
						}

						CookieStore cookieStore = context.getCookieStore();
						List<Cookie> cookies = cookieStore.getCookies();

						result.addAll(cookies);
					}
					finally
					{
						if (response != null)
						{
							response.close();
						}
					}
				}
			}
		}
		catch (Exception ex)
		{
			logger.error("Remote Login failed", ex);
		}

		return result;
	}

	/**
	 * @param onlyEnabledRemoteSystems
	 *            true, wenn nur die aktiven Fremdsysteme geladen werden sollen,
	 *            sonst false
	 * @return Alle Fremdsysteme, wenn false, sonst nur die aktivierten
	 *         Fremdsysteme
	 */
	public List<RemoteSystemDto> getAllRemoteSystems(boolean onlyEnabledRemoteSystems)
	{
		List<RemoteSystem> attachedRemoteSystems = new ArrayList<RemoteSystem>();

		try
		{
			if (!onlyEnabledRemoteSystems)
			{
				remoteSystemRepo.findAll().forEach(e -> attachedRemoteSystems.add(e));
			}
			else
			{
				remoteSystemRepo.findAllEnabledRemoteSystems().forEach(e -> attachedRemoteSystems.add(e));
			}
		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		AutoPopulatingList<RemoteSystemDto> result = new AutoPopulatingList<RemoteSystemDto>(RemoteSystemDto.class);

		for (RemoteSystem attachedRemoteSystem : attachedRemoteSystems)
		{
			result.add(remoteSystemMapper.mapToDto(attachedRemoteSystem));
		}

		return result;
	}

	/**
	 * @param id
	 *            Id des Fremdsystems
	 * @return Das Fremdsystem, welches zur übergebenen Id gehört
	 */
	public RemoteSystemDto getRemoteSystemById(Long id)
	{
		try
		{
			RemoteSystem persistentRemoteSystem = remoteSystemRepo.findOne(id);

			if (persistentRemoteSystem != null)
			{
				RemoteSystemDto result = remoteSystemMapper.mapToDto(persistentRemoteSystem);

				return result;
			}

			return null;
		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		return null;
	}

	/**
	 * @param name
	 *            Name des Fremdsystems
	 * @return Das Fremdsystem, welches zum übergebenen Namen gehört
	 */
	public RemoteSystemDto getRemoteSystemByName(String name)
	{
		try
		{
			RemoteSystem persistentRemoteSystem = null;

			Iterable<RemoteSystem> remoteSystems = remoteSystemRepo.findByName(name);
			if (remoteSystems.iterator().hasNext())
			{
				persistentRemoteSystem = remoteSystems.iterator().next();
			}

			if (persistentRemoteSystem != null)
			{
				RemoteSystemDto result = remoteSystemMapper.mapToDto(persistentRemoteSystem);

				return result;
			}

			return null;
		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		return null;
	}

	/**
	 * @param remoteSystemIds
	 *            Liste mit IDs von Fremdsystemen
	 * @return Liste mit zu den IDs gehörenden Fremdsystemen
	 */
	public List<RemoteSystemDto> getRemoteSystemsForIds(List<Long> remoteSystemIds)
	{
		List<RemoteSystemDto> result = new ArrayList<RemoteSystemDto>();

		try
		{
			Iterable<RemoteSystem> persistentRemoteSystems = remoteSystemRepo.findAll(remoteSystemIds);
			for (RemoteSystem persistentRemoteSystem : persistentRemoteSystems)
			{
				result.add(remoteSystemMapper.mapToDto(persistentRemoteSystem));
			}
		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		return result;
	}

	/**
	 * Speichert das zu übergebende Fremdsystem.
	 * 
	 * @param detachedRemoteSystem
	 *            das zu speichernde Fremdsystem
	 * @return Das gespeicherte Fremdsystem
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public RemoteSystemDto saveRemoteSystem(RemoteSystemDto detachedRemoteSystem)
	{
		try
		{
			RemoteSystem draftRemoteSystem = remoteSystemMapper.mapToEntity(detachedRemoteSystem);

			RemoteSystem persistedRemoteSystem = remoteSystemRepo.save(draftRemoteSystem);

			if (persistedRemoteSystem != null)
			{
				// auf Grund der Benutzerzuordnung alle Logins löschen und pro
				// zugeordneten Benutzer neue
				// anlegen
				{
					List<RemoteLoginDto> oldRemoteLogins = remoteLoginService
							.findAllForRemoteSystemId(persistedRemoteSystem.getId());

					remoteLoginService.deleteRemoteLoginsForRemoteSystemId(persistedRemoteSystem.getId());

					for (Long userId : detachedRemoteSystem.getAssignedUserIdObjects())
					{
						RemoteLoginDto rl = new RemoteLoginDto();
						rl.setEnabled(false);
						rl.setUserId(userId.toString());
						rl.setRemoteSystemId(Long.toString(persistedRemoteSystem.getId()));

						RemoteLoginDto oldEntry = getRelatedOldRemoteLogin(oldRemoteLogins, userId);

						if (oldEntry != null)
						{
							rl.setEnabled(oldEntry.isEnabled());
							rl.setRemoteSystemLoginName(oldEntry.getRemoteSystemLoginName());
							rl.setRemoteSystemPassword(oldEntry.getRemoteSystemPassword());
						}

						remoteLoginService.save(rl);
					}

					for (RemoteLoginDto oldRemoteLogin : oldRemoteLogins)
					{
						remoteLoginService.deleteForId(oldRemoteLogin.getId());
					}
				}

				RemoteSystemDto result = remoteSystemMapper.mapToDto(persistedRemoteSystem);

				return result;
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
	 * Speichert die in der Liste enthaltenen Fremdsysteme.
	 * 
	 * @param detachedRemoteSystems
	 *            Liste mit zu speichernden Fremdsystemen
	 */
	public void saveRemoteSystems(List<RemoteSystemDto> detachedRemoteSystems)
	{
		try
		{
			detachedRemoteSystems.forEach(e -> saveRemoteSystem(e));
		}
		catch (final Exception e)
		{
			logger.error(e);
		}
	}

	/**
	 * Sucht einen Eintrag mit gleicher userId, gibt den Eintrag zurück und
	 * löscht diesen dann in der List.
	 * 
	 * @param oldRemoteLogins
	 *            Liste alter Fremdsystemlogins
	 * @param userId
	 *            BenutzerId
	 * @return null, wenn Eintrag nicht gefunden wird, sonst Kopie des Eintrags
	 */
	private RemoteLoginDto getRelatedOldRemoteLogin(List<RemoteLoginDto> oldRemoteLogins, long userId)
	{
		for (RemoteLoginDto oldRemoteLogin : oldRemoteLogins)
		{
			if (userId == Long.parseLong(oldRemoteLogin.getUserId()))
			{
				RemoteLoginDto result = oldRemoteLogin.copy();
				oldRemoteLogins.remove(oldRemoteLogin);

				return result;
			}
		}

		return null;
	}
}
