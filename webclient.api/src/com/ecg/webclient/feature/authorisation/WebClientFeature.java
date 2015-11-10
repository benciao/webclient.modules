package com.ecg.webclient.feature.authorisation;

/**
 * Basisklasse für ein zu registrierendes Feature. Registriert wird ein Feature
 * ausschließlich bei Anwendungsstart über FeatureService im Webclient-Modul.
 * 
 * @author arndtmar
 */
public abstract class WebClientFeature
{
	String	featureKey;
	boolean	deactivatable;

	protected WebClientFeature(String featureKey, boolean deactivatable)
	{
		this.featureKey = featureKey;
		this.deactivatable = deactivatable;
	}

	public String getFeatureKey()
	{
		return featureKey;
	}

	public boolean isDeactivatable()
	{
		return deactivatable;
	}

	/**
	 * @return Den Pfad zum darzustellenden Icon.
	 */
	public abstract String getIconPath();

	/**
	 * @return Link zum Thymeleaf Template. Wird im Controller verwendet.
	 */
	public abstract String getLink();

	/**
	 * @return i18n Variable.
	 */
	public abstract String getI18nVariable();

	/**
	 * @return Pfad zum Default-Template des Features.
	 */
	public abstract String getLoadingRedirectTemplate();
}
