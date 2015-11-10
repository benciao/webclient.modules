package com.ecg.webclient.feature.administration.authentication;

import org.springframework.security.core.GrantedAuthority;

public class DbGrantedAuthoritiy implements GrantedAuthority
{
    private static final long serialVersionUID = -3442491340287701183L;
    private final String      role;

    public DbGrantedAuthoritiy(String role)
    {
        this.role = role;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (obj instanceof DbGrantedAuthoritiy)
        {
            return this.role.equals(((DbGrantedAuthoritiy) obj).role);
        }

        return false;
    }

    @Override
    public String getAuthority()
    {
        return this.role;
    }

    @Override
    public int hashCode()
    {
        return this.role.hashCode();
    }

    @Override
    public String toString()
    {
        return this.role;
    }
}
