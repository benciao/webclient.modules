package com.ecg.webclient.feature.administration.setup;

import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.authorisation.WebClientFeature;

/**
 * Feature, welches das Administrieren des Gesamtsystems ermöglicht.
 *
 * @author arndtmar
 */
@Component
public class AdministrationFeature extends WebClientFeature
{
    public static final String KEY = "SEC";

    public AdministrationFeature()
    {
        super(KEY, false);
    }

	@Override
	public String getIconPath()
	{
		return "/icons/administration_24_white.png";
	}

	@Override
	public String getLink()
	{
		return "/admin";
	}

	@Override
	public String getI18nVariable()
	{
		return "feature.administration.title";
	}

	@Override
	public String getLoadingRedirectTemplate()
	{
		return "feature/administration/administration";
	}
}
