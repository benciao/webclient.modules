package com.ecg.webclient.feature.administration.persistence.modell;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Entität eines Nutzer-Audit.
 * 
 * @author arndtmar
 */
@Entity
@Table(name = "SEC_AUDIT")
public class Audit
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long    id;
    private Date    occurance;
    private boolean authenticationOk;
    @ManyToOne
    private User    user;

    public Audit()
    {}

    @Transient
    public Audit bind(Audit newAudit)
    {
        setOccurance(newAudit.getOccurance());
        setAuthenticationOk(newAudit.isAuthenticationOk());
        setUser(newAudit.getUser());

        return this;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Audit other = (Audit) obj;
        if (id != other.id) return false;
        return true;
    }

    public long getId()
    {
        return id;
    }

    public Date getOccurance()
    {
        return occurance;
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

    public boolean isAuthenticationOk()
    {
        return authenticationOk;
    }

    public void setAuthenticationOk(boolean authenticationOk)
    {
        this.authenticationOk = authenticationOk;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setOccurance(Date occurance)
    {
        this.occurance = occurance;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

}
