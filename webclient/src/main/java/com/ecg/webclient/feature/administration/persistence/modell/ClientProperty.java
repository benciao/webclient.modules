package com.ecg.webclient.feature.administration.persistence.modell;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Entität einer Eigenschaft.
 * 
 * @author arndtmar
 */
@Entity
@Table(name = "SEC_CLIENT_PROPERTY")
public class ClientProperty
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long   id;
    private String prop_key;
    private String prop_value;
    @ManyToOne
    private Client client;

    public ClientProperty()
    {}

    @Transient
    public ClientProperty bind(ClientProperty newProperty)
    {
        setKey(newProperty.getKey());
        setValue(newProperty.getValue());
        setClient(client);
        newProperty.getClient();

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
        if (!(obj instanceof ClientProperty))
        {
            return false;
        }
        ClientProperty other = (ClientProperty) obj;
        if (id != other.id)
        {
            return false;
        }
        if (prop_key == null)
        {
            if (other.prop_key != null)
            {
                return false;
            }
        }
        else if (!prop_key.equals(other.prop_key))
        {
            return false;
        }
        if (prop_value == null)
        {
            if (other.prop_value != null)
            {
                return false;
            }
        }
        else if (!prop_value.equals(other.prop_value))
        {
            return false;
        }
        return true;
    }

    public Client getClient()
    {
        return client;
    }

    public long getId()
    {
        return id;
    }

    public String getKey()
    {
        return prop_key;
    }

    public String getValue()
    {
        return prop_value;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((prop_key == null) ? 0 : prop_key.hashCode());
        result = prime * result + ((prop_value == null) ? 0 : prop_value.hashCode());
        return result;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    public void setId(long id)
    {
        if (id != -1)
        {
            this.id = id;
        }
    }

    public void setKey(String key)
    {
        this.prop_key = key;
    }

    public void setValue(String value)
    {
        this.prop_value = value;
    }
}
