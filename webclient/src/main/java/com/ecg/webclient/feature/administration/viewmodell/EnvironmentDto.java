package com.ecg.webclient.feature.administration.viewmodell;

/**
 * Implementierung einer von der Persistenz detachten Umgebung.
 * 
 * @author arndtmar
 */
public class EnvironmentDto extends BaseObjectDto
{
    private Integer passwordChangeInterval;
    private Integer allowedLoginAttempts;
    private String  systemIdentifier;

    public EnvironmentDto()
    {}

    public Integer getAllowedLoginAttempts()
    {
        return allowedLoginAttempts;
    }

    public Integer getPasswordChangeInterval()
    {
        return passwordChangeInterval;
    }

    public String getSystemIdentifier()
    {
        return systemIdentifier;
    }

    public void setAllowedLoginAttempts(Integer allowedLoginAttempts)
    {
        this.allowedLoginAttempts = allowedLoginAttempts;
    }

    public void setPasswordChangeInterval(Integer passwordChangeInterval)
    {
        this.passwordChangeInterval = passwordChangeInterval;
    }

    public void setSystemIdentifier(String systemIdentifier)
    {
        this.systemIdentifier = systemIdentifier;
    }
}
