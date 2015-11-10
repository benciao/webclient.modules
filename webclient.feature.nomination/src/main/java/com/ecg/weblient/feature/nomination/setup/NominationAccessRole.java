package com.ecg.weblient.feature.nomination.setup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.authorisation.WebClientAccessRole;

/**
 * Rolle, welche Zugriff auf Abwicklung erlaubt.
 *
 * @author arndtmar
 */
@Component
public class NominationAccessRole extends WebClientAccessRole
{
	public static final String KEY = "NOMINATION";

	@Autowired
	public NominationAccessRole(NominationFeature feature)
	{
		super(feature, KEY);
	}
}
