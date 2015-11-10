package com.ecg.webclient.feature.nomination;

import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.administration.autorisation.WebClientFeature;

@Component
public class FeatureNomination extends WebClientFeature
{
	public static final String KEY = "NOM";

    public FeatureNomination()
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
		return "nomination";
	}

}
