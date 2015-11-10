package com.ecg.webclient.feature.administration.viewmodell;


/**
 * Implementierung eines von der Persistenz detachten Basisobjektes.
 * 
 * @author arndtmar
 */
public class BaseObjectDto
{
    private long    id;
    private boolean delete = false;

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
        if (!(obj instanceof BaseObjectDto))
        {
            return false;
        }
        BaseObjectDto other = (BaseObjectDto) obj;
        if (id != other.id)
        {
            return false;
        }
        return true;
    }

    public long getId()
    {
        return id;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    public boolean isDelete()
    {
        return delete;
    }

    public void setDelete(boolean delete)
    {
        this.delete = delete;
    }

    public void setId(long id)
    {
        this.id = id;
    }
}
