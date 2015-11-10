package com.ecg.webclient.feature.administration.viewmodell;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.util.AutoPopulatingList;

public class ClientProperties
{
    @Valid
    private List<ClientPropertyDto> properties;

    public List<ClientPropertyDto> getProperties()
    {
        if (properties == null)
        {
            properties = new AutoPopulatingList<ClientPropertyDto>(ClientPropertyDto.class);
        }
        return properties;
    }

    public void removeDeleted()
    {
        List<ClientPropertyDto> propertiesToRemove = new ArrayList<ClientPropertyDto>();
        for (ClientPropertyDto property : properties)
        {
            if (property.isDelete())
            {
                propertiesToRemove.add(property);
            }
        }
        properties.removeAll(propertiesToRemove);
    }

    public void setProperties(List<ClientPropertyDto> properties)
    {
        this.properties = properties;
    }
}
