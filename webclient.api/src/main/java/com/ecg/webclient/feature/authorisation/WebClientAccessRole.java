package com.ecg.webclient.feature.authorisation;

/**
 * Basisklasse für eine zu registrierende Rolle. Registriert wird eine Rolle
 * ausschließlich bei Anwendungsstart über RoleService im Webclient-Modul.
 * 
 * @author arndtmar
 */
public abstract class WebClientAccessRole
{
	WebClientFeature	feature;
	String				roleKey;

	protected WebClientAccessRole(WebClientFeature feature, String roleKey)
	{
		this.feature = feature;
		this.roleKey = roleKey;
	}

	/**
	 * @return Name der Rolle zusammengesetzt aus FeatureId und Rollenname
	 */
	public String getName()
	{
		return feature.getFeatureKey().toUpperCase() + "_" + roleKey.toUpperCase();
	}

	public WebClientFeature getFeature()
	{
		return feature;
	}

	public String getRoleKey()
	{
		return roleKey;
	}
}
