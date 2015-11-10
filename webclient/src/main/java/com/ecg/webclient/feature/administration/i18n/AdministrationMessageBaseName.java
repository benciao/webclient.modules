package com.ecg.webclient.feature.administration.i18n;

import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.i18n.WebClientResourceLocation;

@Component
public class AdministrationMessageBaseName extends WebClientResourceLocation
{
	public AdministrationMessageBaseName()
	{
		super ("classpath:/i18n/sec_messages");
	}
}
