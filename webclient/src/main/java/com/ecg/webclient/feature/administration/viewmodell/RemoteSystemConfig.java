package com.ecg.webclient.feature.administration.viewmodell;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.util.AutoPopulatingList;

public class RemoteSystemConfig
{
    @Valid
    private List<RemoteSystemDto> remoteSystems;
    private List<UserDto>         availableUsers;

    public List<UserDto> getAvailableUsers()
    {
        if (availableUsers == null)
        {
            availableUsers = new AutoPopulatingList<UserDto>(UserDto.class);
        }
        return availableUsers;
    }

    public List<RemoteSystemDto> getRemoteSystems()
    {
        if (remoteSystems == null)
        {
            remoteSystems = new AutoPopulatingList<RemoteSystemDto>(RemoteSystemDto.class);
        }
        return remoteSystems;
    }

    public void removeDeleted()
    {
        List<RemoteSystemDto> remoteSystemsToRemove = new ArrayList<RemoteSystemDto>();
        for (RemoteSystemDto remoteSystem : remoteSystems)
        {
            if (remoteSystem.isDelete())
            {
                remoteSystemsToRemove.add(remoteSystem);
            }
        }
        remoteSystems.removeAll(remoteSystemsToRemove);
    }

    public void setAvailableUsers(List<UserDto> availableUsers)
    {
        this.availableUsers = availableUsers;
    }

    public void setRemoteSystems(List<RemoteSystemDto> remoteSystems)
    {
        this.remoteSystems = remoteSystems;
    }
}
