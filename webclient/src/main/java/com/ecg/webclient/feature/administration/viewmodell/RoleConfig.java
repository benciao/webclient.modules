package com.ecg.webclient.feature.administration.viewmodell;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.util.AutoPopulatingList;

public class RoleConfig
{
    @Valid
    private List<RoleDto> roles;

    public List<RoleDto> getRoles()
    {
        if (roles == null)
        {
            roles = new AutoPopulatingList<RoleDto>(RoleDto.class);
        }
        return roles;
    }

    public void removeDeleted()
    {
        List<RoleDto> rolesToRemove = new ArrayList<RoleDto>();
        for (RoleDto role : roles)
        {
            if (role.isDelete())
            {
                rolesToRemove.add(role);
            }
        }
        roles.removeAll(rolesToRemove);
    }

    public void setRoles(List<RoleDto> roles)
    {
        this.roles = roles;
    }
}
