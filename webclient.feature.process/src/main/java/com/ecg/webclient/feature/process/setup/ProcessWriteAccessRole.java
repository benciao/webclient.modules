package com.ecg.webclient.feature.process.setup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.authorisation.WebClientAccessRole;

/**
 * Rolle zum Schreiben im Feature Prozessmonitor.
 *
 * @author arndtmar
 */
@Component
public class ProcessWriteAccessRole extends WebClientAccessRole
{
	public static final String KEY = "WRITE";

	@Autowired
	public ProcessWriteAccessRole(ProcessFeature feature)
	{
		super(feature, KEY);
	}
}
