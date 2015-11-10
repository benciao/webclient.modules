package com.ecg.webclient.feature.administration.persistence.modell;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Entität eine LDAP-Konfiguration.
 * 
 * @author arndtmar
 */
@Entity
@Table(name = "SEC_LDAP_CONFIG")
public class LdapConfig
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long	id;
	private boolean	enabled;
	private String	url;
	private String	username;
	private String	password;
	private String	base;
	private String	filter;

	public LdapConfig()
	{
	}
	
	@Transient
    public LdapConfig bind(LdapConfig ldapConfig)
    {
        setEnabled(ldapConfig.isEnabled());
        setUrl(ldapConfig.getUrl());
        setUsername(ldapConfig.getUsername());
        setPassword(ldapConfig.getPassword());
        setBase(ldapConfig.getBase());
        setFilter(ldapConfig.getFilter());
        
        return this;
    }

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LdapConfig other = (LdapConfig) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getBase()
	{
		return base;
	}

	public void setBase(String base)
	{
		this.base = base;
	}

	public String getFilter()
	{
		return filter;
	}

	public void setFilter(String filter)
	{
		this.filter = filter;
	}
}
