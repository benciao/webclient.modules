package com.ecg.webclient.feature.administration.viewmodell;

/**
 * Implementierung eines von der Persistenz detachten Features.
 * 
 * @author arndtmar
 */
public class FeatureDto extends BaseObjectDto
{
	private String	name;
	private boolean	enabled;
	private boolean	deactivatable;
	private boolean	selected;
	private String	iconPath;
	private String	link;
	private String	i18nVariable;
	private String	loadingRedirectTemplate;

	public FeatureDto()
	{
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public String getIconPath()
	{
		return iconPath;
	}

	public void setIconPath(String iconPath)
	{
		this.iconPath = iconPath;
	}

	public String getLoadingRedirectTemplate()
	{
		return loadingRedirectTemplate;
	}

	public void setLoadingRedirectTemplate(String loadingRedirectTemplate)
	{
		this.loadingRedirectTemplate = loadingRedirectTemplate;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getI18nVariable()
	{
		return i18nVariable;
	}

	public void setI18nVariable(String i18nVariable)
	{
		this.i18nVariable = i18nVariable;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isDeactivatable()
	{
		return deactivatable;
	}

	public void setDeactivatable(boolean deactivatable)
	{
		this.deactivatable = deactivatable;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
}
