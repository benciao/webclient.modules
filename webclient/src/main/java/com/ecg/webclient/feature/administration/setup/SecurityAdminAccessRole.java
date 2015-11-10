package com.ecg.webclient.feature.administration.setup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.administration.autorisation.WebClientAccessRole;

/**
 * Rolle, welche das Administrieren der Benutzer- und Berechtigungsverwaltung ermöglicht.
 * 
 * @author arndtmar
 */
@Component
public class SecurityAdminAccessRole extends WebClientAccessRole
{
    public static final String KEY = "ADMIN";

    @Autowired
    public SecurityAdminAccessRole(AdministrationFeature feature)
    {
        super(feature, KEY);
    }
}
