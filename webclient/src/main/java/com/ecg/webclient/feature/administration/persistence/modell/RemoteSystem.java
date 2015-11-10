package com.ecg.webclient.feature.administration.persistence.modell;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Entität eines Fremdsystems.
 * 
 * @author arndtmar
 */
@Entity
@Table(name = "SEC_REMOTE_SYSTEM")
public class RemoteSystem
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long    id;
    private String  name;
    private String  description;
    private boolean enabled;
    private String  loginUrl;
    private String  loginParameter;
    private String  passwordParameter;
    private String  logoutUrl;
    private boolean doPostRequest;

    public RemoteSystem()
    {}

    @Transient
    public RemoteSystem bind(RemoteSystem newRemoteSystem)
    {
        setName(newRemoteSystem.getName());
        setDescription(newRemoteSystem.getDescription());
        setEnabled(newRemoteSystem.isEnabled());
        setLoginUrl(newRemoteSystem.getLoginUrl());
        setLoginParameter(newRemoteSystem.getLoginParameter());
        setPasswordParameter(newRemoteSystem.getPasswordParameter());
        setLogoutUrl(newRemoteSystem.getLogoutUrl());
        setDoPostRequest(newRemoteSystem.isDoPostRequest());

        return this;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof RemoteSystem))
        {
            return false;
        }
        RemoteSystem other = (RemoteSystem) obj;
        if (id != other.id)
        {
            return false;
        }
        if (loginParameter == null)
        {
            if (other.loginParameter != null)
            {
                return false;
            }
        }
        else if (!loginParameter.equals(other.loginParameter))
        {
            return false;
        }
        if (loginUrl == null)
        {
            if (other.loginUrl != null)
            {
                return false;
            }
        }
        else if (!loginUrl.equals(other.loginUrl))
        {
            return false;
        }
        if (logoutUrl == null)
        {
            if (other.logoutUrl != null)
            {
                return false;
            }
        }
        else if (!logoutUrl.equals(other.logoutUrl))
        {
            return false;
        }
        if (name == null)
        {
            if (other.name != null)
            {
                return false;
            }
        }
        else if (!name.equals(other.name))
        {
            return false;
        }
        if (passwordParameter == null)
        {
            if (other.passwordParameter != null)
            {
                return false;
            }
        }
        else if (!passwordParameter.equals(other.passwordParameter))
        {
            return false;
        }
        return true;
    }

    public String getDescription()
    {
        return description;
    }

    public long getId()
    {
        return id;
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

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((loginParameter == null) ? 0 : loginParameter.hashCode());
        result = prime * result + ((loginUrl == null) ? 0 : loginUrl.hashCode());
        result = prime * result + ((logoutUrl == null) ? 0 : logoutUrl.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((passwordParameter == null) ? 0 : passwordParameter.hashCode());
        return result;
    }

    public boolean isDoPostRequest()
    {
        return doPostRequest;
    }

    public boolean isEnabled()
    {
        return enabled;
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

    public void setId(long id)
    {
        this.id = id;
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
