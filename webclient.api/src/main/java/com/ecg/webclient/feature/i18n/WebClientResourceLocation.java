package com.ecg.webclient.feature.i18n;

public abstract class WebClientResourceLocation
{
	private String messageBaseName;

	public String getMessageBaseName()
	{
		return messageBaseName;
	}

	protected WebClientResourceLocation(String messageBaseName)
	{
		this.messageBaseName = messageBaseName;
	}
}
