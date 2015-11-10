package com.ecg.weblient.feature.nomination.i18n;

import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.i18n.WebClientResourceLocation;

@Component
public class NominationMessageBaseName extends WebClientResourceLocation
{
	public NominationMessageBaseName()
	{
		super("classpath:/i18n/nom_messages");
	}
}
