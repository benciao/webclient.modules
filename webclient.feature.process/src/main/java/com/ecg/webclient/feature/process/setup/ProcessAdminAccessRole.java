package com.ecg.webclient.feature.process.setup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.authorisation.WebClientAccessRole;

/**
 * Rolle zum Administrieren des Features Prozessmonitor.
 *
 * @author arndtmar
 */
@Component
public class ProcessAdminAccessRole extends WebClientAccessRole
{
	public static final String KEY = "ADMIN";

	@Autowired
	public ProcessAdminAccessRole(ProcessFeature feature)
	{
		super(feature, KEY);
	}
}
