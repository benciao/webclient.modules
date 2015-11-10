package com.ecg.webclient.feature.nomination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.administration.autorisation.WebClientAccessRole;

/**
 * Rolle, welche Konfigurieren des Gesamtsystems ermöglicht.
 *
 * @author arndtmar
 */
@Component
public class NominationAccessRole extends WebClientAccessRole
{
	public static final String KEY = "NOMINATION";

	@Autowired
	public NominationAccessRole(FeatureNomination feature)
	{
		super(feature, KEY);
	}
}
