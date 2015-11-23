package com.ecg.webclient.feature.administration.setup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.authorisation.WebClientAccessRole;

/**
 * Rolle, welche das Nutzerbezogene Aktionen ermöglicht.
 * 
 * @author arndtmar
 */
@Component
public class SecurityReadAccessRole extends WebClientAccessRole
{
    public static final String KEY = "USER";

    @Autowired
    public SecurityReadAccessRole(AdministrationFeature feature)
    {
        super(feature, KEY);
    }
}
