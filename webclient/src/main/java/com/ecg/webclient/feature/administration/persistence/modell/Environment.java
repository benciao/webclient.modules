package com.ecg.webclient.feature.administration.persistence.modell;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Entität einer Umgebung.
 * 
 * @author arndtmar
 */
@Entity
@Table(name = "SEC_ENVIRONMENT")
public class Environment
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long   id;
    private int    passwordChangeInterval;
    private int    allowedLoginAttempts;
    private String systemIdentifier;

    public Environment()
    {}

    @Transient
    public Environment bind(Environment newSystem)
    {
        setPasswordChangeInterval(newSystem.getPasswordChangeInterval());
        setAllowedLoginAttempts(newSystem.getAllowedLoginAttempts());
        setSystemIdentifier(newSystem.getSystemIdentifier());
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
        if (!(obj instanceof Environment))
        {
            return false;
        }
        Environment other = (Environment) obj;
        if (id != other.id)
        {
            return false;
        }
        return true;
    }

    public int getAllowedLoginAttempts()
    {
        return allowedLoginAttempts;
    }

    public long getId()
    {
        return id;
    }

    public int getPasswordChangeInterval()
    {
        return passwordChangeInterval;
    }

    public String getSystemIdentifier()
    {
        return systemIdentifier;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    public void setAllowedLoginAttempts(int allowedLoginAttempts)
    {
        this.allowedLoginAttempts = allowedLoginAttempts;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setPasswordChangeInterval(int passwordChangeInterval)
    {
        this.passwordChangeInterval = passwordChangeInterval;
    }

    public void setSystemIdentifier(String systemIdentifier)
    {
        this.systemIdentifier = systemIdentifier;
    }
}
