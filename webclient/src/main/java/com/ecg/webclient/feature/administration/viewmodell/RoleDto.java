package com.ecg.webclient.feature.administration.viewmodell;

import java.util.Comparator;

import javax.validation.constraints.Size;

/**
 * Implementierung einer von der Persistenz detachten Benutzerrolle.
 * 
 * @author arndtmar
 */
public class RoleDto extends BaseObjectDto
{
    public static Comparator<RoleDto> RoleDtoComparator = new Comparator<RoleDto>()
                                                        {

                                                            @Override
                                                            public int compare(RoleDto r1, RoleDto r2)
                                                            {
                                                                return r1.getName().compareTo(r2.getName());
                                                            }

                                                        };

    @Size(min = 4, max = 100)
    private String                    name;
    private boolean                   enabled;
    private FeatureDto                feature;

    public RoleDto()
    {}

    public String getCombinedName()
    {
        return "ROLE_" + this.getFeature().getName() + "_" + this.getName();
    }

    public FeatureDto getFeature()
    {
        return feature;
    }

    public String getName()
    {
        return name;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void setFeature(FeatureDto feature)
    {
        this.feature = feature;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
