package com.ecg.webclient.feature.administration.persistence.modell;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Entität einer BenutzerGruppe.
 * 
 * @author arndtmar
 */
@Entity
@Table(name = "SEC_GROUP")
public class Group
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long       id;
    private String     name;
    private String     description;
    private boolean    enabled;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "SEC_GROUP_SEC_ROLE", joinColumns = @JoinColumn(name = "GROUP_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private List<Role> roles;
    @OneToOne
    private Client     client;

    public Group()
    {}

    @Transient
    public Group bind(Group newGroup)
    {
        setName(newGroup.getName());
        setDescription(newGroup.getDescription());
        setEnabled(newGroup.isEnabled());
        setClient(newGroup.getClient());
        setRoles(newGroup.getRoles());

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
        if (!(obj instanceof Group))
        {
            return false;
        }
        Group other = (Group) obj;
        if (client == null)
        {
            if (other.client != null)
            {
                return false;
            }
        }
        else if (!client.equals(other.client))
        {
            return false;
        }
        if (description == null)
        {
            if (other.description != null)
            {
                return false;
            }
        }
        else if (!description.equals(other.description))
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
        return true;
    }

    public Client getClient()
    {
        return client;
    }

    public String getDescription()
    {
        return description;
    }

    public long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public List<Role> getRoles()
    {
        return roles;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((client == null) ? 0 : client.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void setId(long id)
    {
        if (id != -1)
        {
            this.id = id;
        }
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setRoles(List<Role> roles)
    {
        this.roles = roles;
    }
}
