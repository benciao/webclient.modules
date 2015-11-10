package com.ecg.webclient.feature.administration.persistence.modell;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Entität eines Features.
 *
 * @author arndtmar
 */
@Entity
@Table(name = "SEC_FEATURE")
public class Feature
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long	id;
	@Column(unique = true)
	private String	name;
	boolean			enabled;
	boolean			deactivatable;
	String			iconPath;
	String			link;
	String			i18nVariable;
	String			loadingRedirectTemplate;

	public Feature()
	{
	}

	@Transient
	public Feature bind(Feature newFeature)
	{
		setName(newFeature.getName());
		setEnabled(newFeature.isEnabled());
		setDeactivatable(newFeature.isDeactivatable());
		if (newFeature.getIconPath() != null)
		{
			setIconPath(newFeature.getIconPath());
		}
		if (newFeature.getLink() != null)
		{
			setLink(newFeature.getLink());
		}
		if (newFeature.getI18nVariable() != null)
		{
			setI18nVariable(newFeature.getI18nVariable());
		}
		if (newFeature.getLoadingRedirectTemplate() != null)
		{
			setLoadingRedirectTemplate(newFeature.getLoadingRedirectTemplate());
		}

		return this;
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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public String getLoadingRedirectTemplate()
	{
		return loadingRedirectTemplate;
	}

	public void setLoadingRedirectTemplate(String loadingRedirectTemplate)
	{
		this.loadingRedirectTemplate = loadingRedirectTemplate;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Feature other = (Feature) obj;
		if (id != other.id)
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		if (id != -1)
		{
			this.id = id;
		}
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getIconPath()
	{
		return iconPath;
	}

	public void setIconPath(String iconPath)
	{
		this.iconPath = iconPath;
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
}
