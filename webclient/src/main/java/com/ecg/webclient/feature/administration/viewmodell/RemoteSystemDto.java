package com.ecg.webclient.feature.administration.viewmodell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementierung eines von der Persistenz detachten Fremdsystems.
 * 
 * @author arndtmar
 */
public class RemoteSystemDto extends BaseObjectDto
{
    private String  name;
    private String  description;
    private boolean enabled;
    private String  loginUrl;
    private String  loginParameter;
    private String  passwordParameter;
    private String  logoutUrl;
    private String  assignedUserIds;
    private boolean doPostRequest;

    public RemoteSystemDto()
    {}

    public List<Long> getAssignedUserIdObjects()
    {
        List<Long> result = new ArrayList<Long>();

        if (assignedUserIds != null && !assignedUserIds.isEmpty())
        {
            List<String> ids = Arrays.asList(assignedUserIds.split(","));

            for (String id : ids)
            {
                result.add(Long.parseLong(id));
            }
        }

        return result;
    }

    public String getAssignedUserIds()
    {
        return assignedUserIds;
    }

    public String getDescription()
    {
        return description;
    }

    public String getLoginParameter()
    {
        return loginParameter;
    }

    public String getLoginUrl()
    {
        return loginUrl;
    }

    public String getLogoutUrl()
    {
        return logoutUrl;
    }

    public String getName()
    {
        return name;
    }

    public String getPasswordParameter()
    {
        return passwordParameter;
    }

    public boolean isDoPostRequest()
    {
        return doPostRequest;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setAssignedUserIds(String assignedUserIds)
    {
        this.assignedUserIds = assignedUserIds;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setDoPostRequest(boolean doPostRequest)
    {
        this.doPostRequest = doPostRequest;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void setLoginParameter(String loginParameter)
    {
        this.loginParameter = loginParameter;
    }

    public void setLoginUrl(String loginUrl)
    {
        this.loginUrl = loginUrl;
    }

    public void setLogoutUrl(String logoutUrl)
    {
        this.logoutUrl = logoutUrl;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPasswordParameter(String passwordParameter)
    {
        this.passwordParameter = passwordParameter;
    }
}
