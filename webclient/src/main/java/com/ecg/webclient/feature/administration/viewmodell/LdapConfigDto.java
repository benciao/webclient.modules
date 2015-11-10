package com.ecg.webclient.feature.administration.viewmodell;

/**
 * Implementierung einer von der Persistenz detachten LDAP-Konfiguration.
 * 
 * @author arndtmar
 */
public class LdapConfigDto extends BaseObjectDto
{
    private boolean enabled;
    private String  url;
    private String  username;
    private String  password;
    private String  base;
    private String  filter;
    private Boolean connectionSuccessful;

    public LdapConfigDto()
    {}

    public String getBase()
    {
        return base;
    }

    public String getFilter()
    {
        return filter;
    }

    public String getPassword()
    {
        return password;
    }

    public String getUrl()
    {
        return url;
    }

    public String getUsername()
    {
        return username;
    }

    public Boolean isConnectionSuccessful()
    {
        return connectionSuccessful;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setBase(String base)
    {
        this.base = base;
    }

    public void setConnectionSuccessful(Boolean connectionSuccessful)
    {
        this.connectionSuccessful = connectionSuccessful;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void setFilter(String filter)
    {
        this.filter = filter;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
