package com.ecg.webclient.feature.administration.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.authentication.AuthenticationUtil;
import com.ecg.webclient.feature.administration.persistence.mapper.ClientMapper;
import com.ecg.webclient.feature.administration.persistence.mapper.GroupMapper;
import com.ecg.webclient.feature.administration.persistence.modell.Group;
import com.ecg.webclient.feature.administration.persistence.repo.ClientRepository;
import com.ecg.webclient.feature.administration.persistence.repo.GroupRepository;
import com.ecg.webclient.feature.administration.persistence.repo.RoleRepository;
import com.ecg.webclient.feature.administration.viewmodell.ClientDto;
import com.ecg.webclient.feature.administration.viewmodell.GroupDto;

/**
 * Service zum Bearbeiten von Gruppen.
 * 
 * @author arndtmar
 */
@Component
public class GroupService
{
	static final Logger logger = LogManager.getLogger(GroupService.class.getName());

	@Autowired
	ClientRepository	clientRepo;
	@Autowired
	GroupRepository		groupRepo;
	@Autowired
	RoleRepository		roleRepo;
	@Autowired
	GroupMapper			groupMapper;
	@Autowired
	ClientMapper		clientMapper;

	public GroupService()
	{
	}

	/**
	 * Löscht die in der Liste enthaltenen Gruppen.
	 * 
	 * @param detachedGroups
	 *            Liste von zu löschenden Gruppen
	 */
	public void deleteGroups(List<GroupDto> detachedGroups)
	{
		try
		{
			for (GroupDto group : detachedGroups)
			{
				Group persistentGroup = groupRepo.findOne(group.getId());

				if (persistentGroup != null)
				{
					groupRepo.delete(persistentGroup);
				}
			}
		}
		catch (final Exception e)
		{
			logger.error(e);
		}
	}

	/**
	 * @param onlyEnabled
	 *            true, wenn nur die aktiven Gruppen geladen werden sollen,
	 *            sonst false
	 * @return Alle Gruppen, wenn false, sonst nur die aktivierten Gruppen
	 */
	public List<GroupDto> getAllGroups(boolean onlyEnabledGroups)
	{
		List<Group> attachedGroups = new ArrayList<Group>();

		try
		{
			if (!onlyEnabledGroups)
			{
				groupRepo.findAll().forEach(e -> attachedGroups.add(e));
			}
			else
			{
				groupRepo.findAllEnabledGroups(true).forEach(e -> attachedGroups.add(e));
			}
		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		AutoPopulatingList<GroupDto> result = new AutoPopulatingList<GroupDto>(GroupDto.class);
		for (Group attachedGroup : attachedGroups)
		{
			result.add(groupMapper.mapToDto(attachedGroup));
		}

		return result;
	}

	/**
	 * @param clientId
	 *            Mandanten-ID
	 * @return Alle zum Mandanten gehörende Gruppen
	 */
	public List<GroupDto> getAllGroupsForClient(Long clientId)
	{
		List<Group> attachedGroups = new ArrayList<Group>();

		try
		{
			groupRepo.findAllGroupsAssignedToClientId(clientId).forEach(e -> attachedGroups.add(e));
		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		AutoPopulatingList<GroupDto> result = new AutoPopulatingList<GroupDto>(GroupDto.class);
		for (Group attachedGroup : attachedGroups)
		{
			result.add(groupMapper.mapToDto(attachedGroup));
		}

		return result;
	}

	/**
	 * @param groupId
	 *            Gruppen-ID
	 * @return Der der Gruppe zugeordnete Mandant
	 */
	public ClientDto getClientForGroupId(Long groupId)
	{
		try
		{
			Group persistentGroup = groupRepo.findOne(groupId);
			if (persistentGroup != null)
			{
				return clientMapper.mapToDto(persistentGroup.getClient());
			}
		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		return null;
	}

	/**
	 * @param groupIdObjects
	 *            Liste von GruppenIds
	 * @return Eine Liste aller aktivierten Gruppen
	 */
	public List<GroupDto> getEnabledGroupsForIds(List<Long> groupIdObjects)
	{
		List<Group> attachedGroups = new ArrayList<Group>();

		try
		{
			groupRepo.findEnabledGroupsForId(groupIdObjects).forEach(e -> attachedGroups.add(e));
		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		AutoPopulatingList<GroupDto> result = new AutoPopulatingList<GroupDto>(GroupDto.class);
		for (Group attachedGroup : attachedGroups)
		{
			result.add(groupMapper.mapToDto(attachedGroup));
		}

		return result;
	}

	/**
	 * @param name
	 *            Gruppenname
	 * @return Die zum Namen gehörende Gruppe
	 */
	public GroupDto getGroupByName(String name)
	{
		try
		{
			Group persistentGroup = null;

			Iterable<Group> groups = groupRepo.findGroupByName(name);
			if (groups.iterator().hasNext())
			{
				persistentGroup = groups.iterator().next();
			}

			return (persistentGroup != null) ? groupMapper.mapToDto(persistentGroup) : null;
		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		return null;
	}

	/**
	 * Ermittelt, ob es für den aktuell selektierten Mandanten eine Gruppe mit
	 * dem als Parameter übergebenen Namen gibt.
	 * 
	 * @param name
	 *            Gruppenname
	 * @return Die zum Namen gehörende Gruppe
	 */
	public GroupDto getGroupByNameForCurrentClient(String name, Long clientId)
	{
		try
		{
			Group persistentGroup = groupRepo.findGroupByNameForClient(name, clientId);

			return (persistentGroup != null) ? groupMapper.mapToDto(persistentGroup) : null;
		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		return null;
	}

	/**
	 * @param groupIds
	 *            Liste von Gruppen-IDs
	 * @return Liste von Gruppen
	 */
	public List<GroupDto> getGroupsForIds(List<Long> groupIds)
	{
		List<GroupDto> result = new ArrayList<GroupDto>();

		try
		{
			Iterable<Group> persistentGroups = groupRepo.findAll(groupIds);
			for (Group persistentGroup : persistentGroups)
			{
				result.add(groupMapper.mapToDto(persistentGroup));
			}

		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		return result;
	}

	/**
     * Speichert eine Gruppe.
     * 
     * @param detachedGroup
     *            Zu speichernde Gruppe
     * @return Gespeicherte Gruppe
     */
    public GroupDto saveGroup(GroupDto detachedGroup)
    {
        try
        {
            Group draftGroup = groupMapper.mapToEntity(detachedGroup);
            Group persistedGroup = groupRepo.save(draftGroup);

            if (persistedGroup != null)
            {
                return groupMapper.mapToDto(persistedGroup);
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
	 * Speichert eine Gruppe.
	 * 
	 * @param detachedGroup
	 *            Zu speichernde Gruppe
	 * @return Gespeicherte Gruppe
	 */
	public GroupDto saveGroup(GroupDto detachedGroup, AuthenticationUtil authenticationUtil)
	{
		try
		{
			if (authenticationUtil.getSelectedClient() != null)
			{
				detachedGroup.setClient(authenticationUtil.getSelectedClient());
			}

			Group draftGroup = groupMapper.mapToEntity(detachedGroup);
			Group persistedGroup = groupRepo.save(draftGroup);

			if (persistedGroup != null)
			{
				return groupMapper.mapToDto(persistedGroup);
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
	 * Speichert eine Liste von Gruppen.
	 * 
	 * @param detachedGroups
	 *            Liste von zu speichernden Gruppen
	 */
	public void saveGroups(List<GroupDto> detachedGroups, AuthenticationUtil authenticationUtil)
	{
		try
		{
			detachedGroups.forEach(e -> saveGroup(e, authenticationUtil));
		}
		catch (final Exception e)
		{
			logger.error(e);
		}
	}
}
