package com.ecg.webclient.feature.administration.service;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.administration.persistence.mapper.AuditMapper;
import com.ecg.webclient.feature.administration.persistence.mapper.UserMapper;
import com.ecg.webclient.feature.administration.persistence.modell.Audit;
import com.ecg.webclient.feature.administration.persistence.repo.AuditRepository;
import com.ecg.webclient.feature.administration.viewmodell.UserDto;

/**
 * Service zum Behandeln von Audit-Einträgen.
 * 
 * @author arndtmar
 */
@Component
public class AuditService
{
    static final Logger     logger                     = LogManager.getLogger(AuditService.class.getName());
    static final String     PROPERTY_NAME_ENABLE_AUDIT = "sec.enable.audit";

    private AuditRepository auditRepo;
    private AuditMapper     auditMapper;
    private UserMapper      userMapper;
    Environment             env;

    @Autowired
    public AuditService(AuditRepository auditRepo, AuditMapper auditMapper, UserMapper userMapper,
            Environment env)
    {
        this.auditRepo = auditRepo;
        this.auditMapper = auditMapper;
        this.userMapper = userMapper;
        this.env = env;
    }

    /**
     * Logt einen Anmeldeversuch.
     * 
     * @param user
     *            Benutzer
     * @param authenticationOk
     *            true, wenn Login erfolgreich, sonst false
     */
    public void logLoginAttempt(UserDto user, boolean authenticationOk)
    {
        if (Boolean.parseBoolean(env.getRequiredProperty(PROPERTY_NAME_ENABLE_AUDIT)))
        {
            try
            {
                Audit audit = new Audit();
                audit.setAuthenticationOk(authenticationOk);
                audit.setOccurance(new Date());
                audit.setUser(userMapper.mapToEntity(user));

                auditRepo.save(audit);
            }
            catch (final Exception e)
            {
                logger.error(e);
            }
        }
    }
}
