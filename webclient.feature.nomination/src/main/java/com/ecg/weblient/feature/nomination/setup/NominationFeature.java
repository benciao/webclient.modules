package com.ecg.weblient.feature.nomination.setup;

import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.authorisation.WebClientFeature;

/**
 * Feature, welches Abwicklungsfunktionalität bereithält.
 *
 * @author arndtmar
 */
@Component
public class NominationFeature extends WebClientFeature
{
	public static final String KEY = "NOM";
	
	public NominationFeature()
    {
        super(KEY, true);
    }

	@Override
	public String getIconPath()
	{
		return "/icons/nomination_24_white.png";
	}

	@Override
	public String getLink()
	{
		return "/nom";
	}

	@Override
	public String getI18nVariable()
	{
		return "feature.nomination.title";
	}

	@Override
	public String getLoadingRedirectTemplate()
	{
		return "feature/nomination/nomination";
	}
}
