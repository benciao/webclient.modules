package com.ecg.webclient.feature.administration.persistence.modell;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Entität eines Remote Logins für Fremdsysteme.
 * 
 * @author arndtmar
 */
@Entity
@Table(name = "SEC_REMOTE_LOGIN")
public class RemoteLogin
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long         id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User         user;

    @ManyToOne
    @JoinColumn(name = "REMOTE_SYSTEM_ID")
    private RemoteSystem remoteSystem;

    private boolean      enabled;
    private String       remoteSystemLoginName;
    private String       remoteSystemPassword;

    public RemoteLogin()
    {}

    @Transient
    public RemoteLogin bind(RemoteLogin newRemoteLogin)
    {
        setEnabled(newRemoteLogin.isEnabled());
        setUser(newRemoteLogin.getUser());
        setRemoteSystem(newRemoteLogin.getRemoteSystem());
        setRemoteSystemLoginName(newRemoteLogin.getRemoteSystemLoginName());
        setRemoteSystemPassword(newRemoteLogin.getRemoteSystemPassword());

        return this;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RemoteLogin other = (RemoteLogin) obj;
        if (id != other.id) return false;
        return true;
    }

    public long getId()
    {
        return id;
    }

    public RemoteSystem getRemoteSystem()
    {
        return remoteSystem;
    }

    public String getRemoteSystemLoginName()
    {
        return remoteSystemLoginName;
    }

    public String getRemoteSystemPassword()
    {
        return remoteSystemPassword;
    }

    public User getUser()
    {
        return user;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setRemoteSystem(RemoteSystem remoteSystem)
    {
        this.remoteSystem = remoteSystem;
    }

    public void setRemoteSystemLoginName(String remoteSystemLoginName)
    {
        this.remoteSystemLoginName = remoteSystemLoginName;
    }

    public void setRemoteSystemPassword(String remoteSystemPassword)
    {
        this.remoteSystemPassword = remoteSystemPassword;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

}
