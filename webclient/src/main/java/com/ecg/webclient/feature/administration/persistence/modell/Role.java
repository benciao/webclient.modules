package com.ecg.webclient.feature.administration.persistence.modell;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Entität einer Benutzerrolle.
 * 
 * @author arndtmar
 */
@Entity
@Table(name = "SEC_ROLE")
public class Role
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long	id;
	private String	name;
	@ManyToOne
	@JoinColumn(name = "feature_id")
	private Feature	feature;
	private boolean	enabled;

	public Role()
	{
	}

	public Feature getFeature()
	{
		return feature;
	}

	public void setFeature(Feature feature)
	{
		this.feature = feature;
	}

	@Transient
	public Role bind(Role newRole)
	{
		setName(newRole.getName());
		setEnabled(newRole.isEnabled());
		setFeature(newRole.getFeature());

		return this;
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
		Role other = (Role) obj;
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

	public String getName()
	{
		return name;
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

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public void setId(long id)
	{
		if (id != -1)
		{
			this.id = id;
		}
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
