package com.ecg.webclient.feature.administration.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.persistence.modell.Group;
import com.ecg.webclient.feature.administration.persistence.modell.User;
import com.ecg.webclient.feature.administration.persistence.repo.ClientRepository;
import com.ecg.webclient.feature.administration.persistence.repo.GroupRepository;
import com.ecg.webclient.feature.administration.persistence.repo.RemoteSystemRepository;
import com.ecg.webclient.feature.administration.persistence.repo.UserRepository;
import com.ecg.webclient.feature.administration.viewmodell.UserDto;

/**
 * Mapped die Eigenschaften einer der Persistenz bekannten Entität auf einen detachten Benutzer oder
 * umgekehrt.
 * 
 * @author arndtmar
 */
@Component
public class UserMapper
{
    @Autowired
    ClientRepository       clientRepo;
    @Autowired
    GroupRepository        groupRepo;
    @Autowired
    UserRepository         userRepo;
    @Autowired
    RemoteSystemRepository remoteSystemRepo;

    /**
     * Wandelt einen attachten Benutzer in einen detachten um.
     * 
     * @param user
     *            attachten Benutzer
     * @return Detacheter Benutzer
     */
    public UserDto mapToDto(User user)
    {
        UserDto dto = new UserDto();
        dto.setLogin(user.getLogin());
        dto.setInternal(user.isInternal());
        dto.setLastname(user.getLastname());
        dto.setFirstname(user.getFirstname());
        dto.setEnabled(user.isEnabled());
        dto.setDelete(false);
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setChangePasswordOnNextLogin(user.isChangePasswordOnNextLogin());
        dto.setPasswortChangedTimeStamp(user.getPasswordChangedTimeStamp());
        dto.setLoginAttempts(user.getLoginAttempts());
        dto.setAccountLocked(user.isAccountLocked());

        if (user.getDefaultClient() != null)
        {
            dto.setDefaultClient(Long.toString(user.getDefaultClient().getId()));
        }

        if (user.getGroups() != null)
        {
            String groups = "";
            for (Group group : user.getGroups())
            {
                if (groups.length() == 0)
                {
                    groups = Long.toString(group.getId());
                }
                else
                {
                    groups = groups + "," + group.getId();
                }
            }
            dto.setGroupIds(groups);
        }

        return dto;
    }

    /**
     * Wandelt eine Liste von attachten Benutzern in eine Liste von detachten Benutzern um.
     * 
     * @param users
     *            Liste von attachten Benutzern
     * @return Liste von detachten Benutzern
     */
    public List<UserDto> mapToDtos(List<User> users)
    {
        List<UserDto> result = new AutoPopulatingList<UserDto>(UserDto.class);

        users.forEach(e -> result.add(mapToDto(e)));

        return result;
    }

    /**
     * Wandelt einen detachten Benutzer in einen attachten um.
     * 
     * @param dto
     *            Detachter Benutzer
     * @return attachter Benutzer
     */
    public User mapToEntity(UserDto dto)
    {
        User entity = new User();
        entity.setId(dto.getId());
        entity.setLogin(dto.getLogin());
        entity.setInternal(dto.isInternal());
        entity.setLastname(dto.getLastname());
        entity.setFirstname(dto.getFirstname());
        entity.setEnabled(dto.isEnabled());
        entity.setAccountLocked(dto.isAccountLocked());
        entity.setEmail(dto.getEmail());
        entity.setLoginAttempts(dto.getLoginAttempts());
        entity.setPassword(dto.getPassword());
        entity.setChangePasswordOnNextLogin(dto.isChangePasswordOnNextLogin());
        if (dto.getDefaultClient() != null && !dto.getDefaultClient().isEmpty())
        {
            entity.setDefaultClient(clientRepo.findOne(Long.parseLong(dto.getDefaultClient())));
        }

        List<Group> groups = new ArrayList<Group>();
        groupRepo.findAll(dto.getGroupIdObjects()).forEach(e -> groups.add(e));

        entity.setGroups(groups);

        User persistentUser = userRepo.findOne(entity.getId());
        if (persistentUser != null)
        {
            return persistentUser.bind(entity);
        }

        return entity;
    }
}
