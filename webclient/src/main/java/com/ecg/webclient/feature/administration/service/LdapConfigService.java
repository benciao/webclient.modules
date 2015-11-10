package com.ecg.webclient.feature.administration.service;

import javax.naming.ldap.LdapName;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.administration.persistence.mapper.LdapConfigMapper;
import com.ecg.webclient.feature.administration.persistence.modell.LdapConfig;
import com.ecg.webclient.feature.administration.persistence.repo.LdapConfigRepository;
import com.ecg.webclient.feature.administration.viewmodell.LdapConfigDto;

/**
 * Service zum Bearbeiten von LDAP-Konfigurationen und deren Eigenschaften.
 * 
 * @author arndtmar
 */
@Component
public class LdapConfigService
{
    static final Logger  logger = LogManager.getLogger(LdapConfigService.class.getName());

    LdapConfigRepository ldapConfigRepo;
    LdapConfigMapper     ldapConfigMapper;
    LdapTemplate         ldapTemplate;

    @Autowired
    public LdapConfigService(LdapConfigRepository ldapConfigRepo, LdapConfigMapper ldapConfigMapper,
            LdapTemplate ldapTemplate)
    {
        this.ldapConfigRepo = ldapConfigRepo;
        this.ldapConfigMapper = ldapConfigMapper;
        this.ldapTemplate = ldapTemplate;
    }

    /**
     * @return Die aktuelle LDAP-Konfiguration
     */
    public LdapConfigDto getLdapConfig()
    {
        try
        {
            Iterable<LdapConfig> ldapConfig = ldapConfigRepo.findAll();
            return (ldapConfig != null && ldapConfig.iterator().hasNext()) ? ldapConfigMapper
                    .mapToDto(ldapConfig.iterator().next()) : null;
        }
        catch (final Exception e)
        {
            logger.error(e);
        }

        return null;
    }

    /**
     * @param detachedLdapConfig
     *            aktuelle LDAP Konfiguration
     * @return true, wenn Verbindung zum LDAP-Server mit dieser Konfiguration hergestellt werden kann, sonst
     *         false
     */
    public boolean isLdapConfigOk(LdapConfigDto detachedLdapConfig)
    {
        setupLdapConnection(detachedLdapConfig);

        try
        {
            Object[] filterParams =
            { "dummyLogin" };
            Filter filter = LdapQueryBuilder.query().filter(detachedLdapConfig.getFilter(), filterParams)
                    .filter();

            LdapName

            base = LdapUtils.newLdapName(detachedLdapConfig.getBase());

            ldapTemplate.authenticate(base, filter.encode(), "blablub");

            return true;
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    /**
     * Speichert eine LDAP-konfiguration
     * 
     * @param detachedLdapConfig
     *            zu aktualisierende LDAP-Konfiguration
     * @return Aktualisierte LDAP-Konfiguration
     */
    public LdapConfigDto saveLdapConfig(LdapConfigDto detachedLdapConfig)
    {
        try
        {
            LdapConfig draftLdapConfig = ldapConfigMapper.mapToEntity(detachedLdapConfig);
            LdapConfig persistedLdapConfig = ldapConfigRepo.save(draftLdapConfig);

            if (persistedLdapConfig != null)
            {
                return ldapConfigMapper.mapToDto(persistedLdapConfig);
            }
            else
            {
                return null;
            }
        }
        catch (final Exception e)
        {
            logger.error(e);
        }

        return null;
    }

    public LdapTemplate setupLdapConnection(LdapConfigDto ldapConfig)
    {
        LdapContextSource ctxSrc = new LdapContextSource();
        ctxSrc.setUrl(ldapConfig.getUrl());
        ctxSrc.setUserDn(ldapConfig.getUsername());
        ctxSrc.setPassword(ldapConfig.getPassword());
        ctxSrc.afterPropertiesSet();

        ldapTemplate = new LdapTemplate(ctxSrc);

        return ldapTemplate;
    }
}
