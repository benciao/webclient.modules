package com.ecg.webclient.feature.administration.viewmodell;

import java.util.Comparator;

/**
 * Implementierung eines von der Persistenz detachten Features.
 * 
 * @author arndtmar
 */
public class FeatureDto extends BaseObjectDto
{
    public static Comparator<FeatureDto> FeatureDtoComparator = new Comparator<FeatureDto>()
                                                              {

                                                                  @Override
                                                                  public int compare(FeatureDto f1,
                                                                          FeatureDto f2)
                                                                  {
                                                                      return f1.getName().compareTo(
                                                                              f2.getName());
                                                                  }

                                                              };

    private String                       name;
    private boolean                      enabled;
    private boolean                      deactivatable;
    private boolean                      selected;
    private String                       iconPath;
    private String                       link;
    private String                       i18nVariable;
    private String                       loadingRedirectTemplate;

    public FeatureDto()
    {}

    public String getI18nVariable()
    {
        return i18nVariable;
    }

    public String getIconPath()
    {
        return iconPath;
    }

    public String getLink()
    {
        return link;
    }

    public String getLoadingRedirectTemplate()
    {
        return loadingRedirectTemplate;
    }

    public String getName()
    {
        return name;
    }

    public boolean isDeactivatable()
    {
        return deactivatable;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setDeactivatable(boolean deactivatable)
    {
        this.deactivatable = deactivatable;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void setI18nVariable(String i18nVariable)
    {
        this.i18nVariable = i18nVariable;
    }

    public void setIconPath(String iconPath)
    {
        this.iconPath = iconPath;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public void setLoadingRedirectTemplate(String loadingRedirectTemplate)
    {
        this.loadingRedirectTemplate = loadingRedirectTemplate;
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
