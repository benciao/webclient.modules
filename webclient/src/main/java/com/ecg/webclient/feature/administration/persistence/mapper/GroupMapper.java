package com.ecg.webclient.feature.administration.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.persistence.modell.Group;
import com.ecg.webclient.feature.administration.persistence.modell.Role;
import com.ecg.webclient.feature.administration.persistence.repo.GroupRepository;
import com.ecg.webclient.feature.administration.persistence.repo.RoleRepository;
import com.ecg.webclient.feature.administration.viewmodell.GroupDto;

/**
 * Mapped die Eigenschaften einer der Persistenz bekannten Entität auf eine detachted Gruppe oder umgekehrt.
 * 
 * @author arndtmar
 */
@Component
public class GroupMapper
{
    @Autowired
    ClientMapper    clientMapper;
    @Autowired
    RoleRepository  roleRepo;
    @Autowired
    GroupRepository groupRepo;

    /**
     * Wandelt eine attachte Gruppe in eine detachte um.
     * 
     * @param group
     *            attachte Gruppe
     * @return Detachete Gruppe
     */
    public GroupDto mapToDto(Group group)
    {
        GroupDto dto = new GroupDto();
        dto.setDescription(group.getDescription());
        dto.setName(group.getName());
        dto.setEnabled(group.isEnabled());
        dto.setDelete(false);
        dto.setId(group.getId());

        if (group.getRoles() != null)
        {
            String roles = "";
            for (Role role : group.getRoles())
            {
                if (roles.length() == 0)
                {
                    roles = Long.toString(role.getId());
                }
                else
                {
                    roles = roles + "," + role.getId();
                }
            }
            dto.setRoleIds(roles);
        }

        return dto;
    }

    /**
     * Wandelt eine Liste von attachten Gruppen in eine Liste von detachten Gruppen um.
     * 
     * @param groups
     *            Liste von attachten Gruppen
     * @return Liste von detachten Gruppen
     */
    public List<GroupDto> mapToDtos(List<Group> groups)
    {
        List<GroupDto> result = new AutoPopulatingList<GroupDto>(GroupDto.class);

        groups.forEach(e -> result.add(mapToDto(e)));

        return result;
    }

    /**
     * Wandelt eine Liste von detachten Gruppen in eine Liste von attachten Gruppen um.
     * 
     * @param dtos
     *            Liste von detachten Gruppen
     * @return Liste von attachten Gruppen
     */
    public List<Group> mapToEntities(List<GroupDto> dtos)
    {
        List<Group> result = new ArrayList<Group>();

        dtos.forEach(e -> result.add(mapToEntity(e)));

        return result;
    }

    /**
     * Wandelt eine detachte Gruppen in eine attachte um.
     * 
     * @param dto
     *            Detachte Gruppe
     * @return Attachte Gruppe
     */
    public Group mapToEntity(GroupDto dto)
    {
        Group entity = new Group();
        entity.setId(dto.getId());
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        entity.setEnabled(dto.isEnabled());
        entity.setClient(clientMapper.mapToEntity(dto.getClient()));

        List<Role> roles = new ArrayList<Role>();
        roleRepo.findAll(dto.getRoleIdObjects()).forEach(e -> roles.add(e));

        entity.setRoles(roles);

        Group persistentGroup = groupRepo.findOne(entity.getId());
        if (persistentGroup != null)
        {
            return persistentGroup.bind(entity);
        }

        return entity;
    }
}
