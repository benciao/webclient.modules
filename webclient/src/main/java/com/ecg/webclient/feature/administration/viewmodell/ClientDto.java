package com.ecg.webclient.feature.administration.viewmodell;


/**
 * Implementierung eines von der Persistenz detachten Mandanten.
 * 
 * @author arndtmar
 */
public class ClientDto extends BaseObjectDto
{
    private String            color;

    private String            description;
    private String            name;
    private boolean           enabled;
    private boolean           selected;

    public ClientDto()
    {}

    public String getColor()
    {
        return color;
    }

    public String getDescription()
    {
        return description;
    }

    public String getName()
    {
        return name;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

}
