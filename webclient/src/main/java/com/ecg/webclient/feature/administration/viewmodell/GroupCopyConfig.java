package com.ecg.webclient.feature.administration.viewmodell;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

public class GroupCopyConfig
{
    @Valid
    private GroupDto      copyGroup;
    private List<RoleDto> roles;
    private long          clientId;

    public long getClientId()
    {
        return clientId;
    }

    public GroupDto getCopyGroup()
    {
        return copyGroup;
    }

    public List<RoleDto> getRoles()
    {
        if (roles == null)
        {
            roles = new ArrayList<RoleDto>();
        }
        return roles;
    }

    public void setClientId(long clientId)
    {
        this.clientId = clientId;
    }

    public void setCopyGroup(GroupDto group)
    {
        this.copyGroup = group;
    }

    public void setRoles(List<RoleDto> roles)
    {
        this.roles = roles;
    }
}
