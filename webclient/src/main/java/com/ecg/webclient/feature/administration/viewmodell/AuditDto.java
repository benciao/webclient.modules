package com.ecg.webclient.feature.administration.viewmodell;

import java.util.Date;

/**
 * Implementierung eines von der Persistenz detachten Audit-Eintrages.
 * 
 * @author arndtmar
 */
public class AuditDto extends BaseObjectDto
{
    private long    id;
    private Date    occurance;
    private boolean authenticationOk;
    private UserDto user;

    public AuditDto()
    {}

    @Override
    public long getId()
    {
        return id;
    }

    public Date getOccurance()
    {
        return occurance;
    }

    public UserDto getUser()
    {
        return user;
    }

    public boolean isAuthenticationOk()
    {
        return authenticationOk;
    }

    public void setAuthenticationOk(boolean authenticationOk)
    {
        this.authenticationOk = authenticationOk;
    }

    @Override
    public void setId(long id)
    {
        this.id = id;
    }

    public void setOccurance(Date occurance)
    {
        this.occurance = occurance;
    }

    public void setUser(UserDto user)
    {
        this.user = user;
    }

}
