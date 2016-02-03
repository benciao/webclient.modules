package com.ecg.webclient.feature.administration.viewmodell;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.util.AutoPopulatingList;

public class GroupConfig
{
    @Valid
    private List<GroupDto> groups;
    private List<RoleDto>  roles;
    private GroupDto       copyGroup;
    private long           clientId;
    private boolean        doCopy = false;

    public long getClientId()
    {
        return clientId;
    }

    public GroupDto getCopyGroup()
    {
        return copyGroup;
    }

    public List<GroupDto> getGroups()
    {
        if (groups == null)
        {
            groups = new AutoPopulatingList<GroupDto>(GroupDto.class);
        }
        return groups;
    }

    public List<RoleDto> getRoles()
    {
        if (roles == null)
        {
            roles = new ArrayList<RoleDto>();
        }
        return roles;
    }

    public boolean isDoCopy()
    {
        return doCopy;
    }

    public void removeDeleted()
    {
        List<GroupDto> groupsToRemove = new ArrayList<GroupDto>();
        for (GroupDto group : groups)
        {
            if (group.isDelete())
            {
                groupsToRemove.add(group);
            }
        }
        groups.removeAll(groupsToRemove);
    }

    public void setClientId(long clientId)
    {
        this.clientId = clientId;
    }

    public void setCopyGroup(GroupDto group)
    {
        this.copyGroup = group;
    }

    public void setDoCopy(boolean doCopy)
    {
        this.doCopy = doCopy;
    }

    public void setGroups(List<GroupDto> groups)
    {
        this.groups = groups;
    }

    public void setRoles(List<RoleDto> roles)
    {
        this.roles = roles;
    }
}
