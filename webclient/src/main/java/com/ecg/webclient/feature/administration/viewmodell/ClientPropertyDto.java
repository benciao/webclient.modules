package com.ecg.webclient.feature.administration.viewmodell;


/**
 * Implementierung einer von der Persistenz detachten Eigenschaft.
 * 
 * @author arndtmar
 */
public class ClientPropertyDto extends BaseObjectDto
{
    private String    key;
    private String    value;
    private ClientDto client;

    public ClientDto getClient()
    {
        return client;
    }

    public String getKey()
    {
        return key;
    }

    public String getValue()
    {
        return value;
    }

    public void setClient(ClientDto client)
    {
        this.client = client;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
