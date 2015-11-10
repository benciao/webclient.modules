package com.ecg.webclient.feature.administration.viewmodell;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.util.AutoPopulatingList;

public class UserConfig
{
    @Valid
    private List<UserDto> users;

    public List<UserDto> getUsers()
    {
        if (users == null)
        {
            users = new AutoPopulatingList<UserDto>(UserDto.class);
        }
        return users;
    }

    public void removeDeleted()
    {
        List<UserDto> usersToRemove = new ArrayList<UserDto>();
        for (UserDto user : users)
        {
            if (user.isDelete())
            {
                usersToRemove.add(user);
            }
        }
        users.removeAll(usersToRemove);
    }

    public void setUsers(List<UserDto> users)
    {
        this.users = users;
    }
}
