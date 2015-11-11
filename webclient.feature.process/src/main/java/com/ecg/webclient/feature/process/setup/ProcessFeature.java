package com.ecg.webclient.feature.process.setup;

import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.authorisation.WebClientFeature;

/**
 * Feature zum Nutzen der Dialoge für die Prozesssteuerung.
 *
 * @author arndtmar
 */
@Component
public class ProcessFeature extends WebClientFeature
{
	public static final String KEY = "PROCESS";

	public ProcessFeature()
	{
		super(KEY, true);
	}

	@Override
	public String getIconPath()
	{
		return "/icons/process_24_white.png";
	}

	@Override
	public String getLink()
	{
		return "/process";
	}

	@Override
	public String getI18nVariable()
	{
		return "feature.process.title";
	}

	@Override
	public String getLoadingRedirectTemplate()
	{
		return "feature/process/process";
	}
}
