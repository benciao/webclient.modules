package com.ecg.webclient.feature.administration.viewmodell;

import javax.validation.constraints.Size;

/**
 * Implementierung einer von der Persistenz detachten Benutzerrolle.
 * 
 * @author arndtmar
 */
public class RoleDto extends BaseObjectDto
{
	@Size(min = 4, max = 100)
	private String		name;
	private boolean		enabled;
	private FeatureDto	feature;

	public FeatureDto getFeature()
	{
		return feature;
	}

	public void setFeature(FeatureDto feature)
	{
		this.feature = feature;
	}

	public RoleDto()
	{
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

	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getCombinedName()
	{
		return "ROLE_" + this.getFeature().getName() + "_" + this.getName();
	}
}
