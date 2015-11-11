package com.ecg.webclient.feature.process.setup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.authorisation.WebClientAccessRole;

/**
 * Rolle zum Lesen im Feature Prozessmonitor.
 *
 * @author arndtmar
 */
@Component
public class ProcessReadAccessRole extends WebClientAccessRole
{
	public static final String KEY = "READ";

	@Autowired
	public ProcessReadAccessRole(ProcessFeature feature)
	{
		super(feature, KEY);
	}
}
