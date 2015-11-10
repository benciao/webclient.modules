package com.ecg.webclient.feature.administration.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.ldap.LdapName;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.authentication.PasswordEncoder;
import com.ecg.webclient.feature.administration.persistence.mapper.ClientMapper;
import com.ecg.webclient.feature.administration.persistence.mapper.UserMapper;
import com.ecg.webclient.feature.administration.persistence.modell.User;
import com.ecg.webclient.feature.administration.persistence.repo.ClientRepository;
import com.ecg.webclient.feature.administration.persistence.repo.GroupRepository;
import com.ecg.webclient.feature.administration.persistence.repo.UserRepository;
import com.ecg.webclient.feature.administration.viewmodell.ClientDto;
import com.ecg.webclient.feature.administration.viewmodell.LdapConfigDto;
import com.ecg.webclient.feature.administration.viewmodell.UserDto;

/**
 * Service zum Bearbeiten von Benutzern.
 * 
 * @author arndtmar
 */
@Component
public class UserService
{
    static final Logger logger                           = LogManager.getLogger(UserService.class.getName());
    static final String PROPERTY_NAME_INIT_USER_PASSWORD = "sec.init.user.pw";

    UserRepository      userRepo;
    GroupRepository     groupRepo;
    ClientRepository    clientRepo;
    ClientMapper        clientMapper;
    UserMapper          userMapper;
    Environment         env;
    EnvironmentService  environmentService;
    LdapConfigService   ldapConfigService;

    @Autowired
    public UserService(UserRepository userRepo, GroupRepository groupRepo, ClientRepository clientRepo,
            ClientMapper clientMapper, UserMapper userMapper, Environment env,
            EnvironmentService environmentService, LdapConfigService ldapConfigService)
    {
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
        this.clientRepo = clientRepo;
        this.clientMapper = clientMapper;
        this.userMapper = userMapper;
        this.env = env;
        this.environmentService = environmentService;
        this.ldapConfigService = ldapConfigService;
    }

    /**
     * Löscht die in der Liste enthaltenen Benutzer.
     * 
     * @param detachedUsers
     *            Liste von zu löschenden Benutzern
     */
    public void deleteUsers(List<UserDto> detachedUsers)
    {
        try
        {
            for (UserDto user : detachedUsers)
            {
                User persistentUser = userRepo.findOne(user.getId());

                if (persistentUser != null)
                {
                    userRepo.delete(persistentUser);
                }
            }
        }
        catch (final Exception e)
        {
            logger.error(e);
        }
    }

    /**
     * @param onlyEnabledUsers
     *            true, wenn nur die aktiven Benutzer geladen werden sollen, sonst false
     * @return Alle Benutzer, wenn false, sonst nur die aktivierten Benutzer
     */
    public List<UserDto> getAllUsers(boolean onlyEnabledUsers)
    {
        List<User> attachedUsers = new ArrayList<User>();

        try
        {
            if (!onlyEnabledUsers)
            {
                userRepo.findAll().forEach(e -> attachedUsers.add(e));
            }
            else
            {
                userRepo.findAllEnabledUsers(true).forEach(e -> attachedUsers.add(e));
            }
        }
        catch (final Exception e)
        {
            logger.error(e);
        }

        AutoPopulatingList<UserDto> result = new AutoPopulatingList<UserDto>(UserDto.class);

        for (User attachedUser : attachedUsers)
        {
            result.add(userMapper.mapToDto(attachedUser));
        }

        return result;
    }

    /**
     * @param user
     *            Benutzer
     * @return Den dem Benutzer zugeordneten Standardmandanten
     */
    public ClientDto getDefaultClientForUser(UserDto user)
    {
        try
        {
            User persistentUser = userRepo.findOne(user.getId());

            if (persistentUser != null)
            {
                return clientMapper.mapToDto(persistentUser.getDefaultClient());
            }
        }
        catch (final Exception e)
        {
            logger.error(e);
        }

        return null;
    }

    /**
     * @param userId
     *            Id des Benutzers
     * @return das Passwort des Benutzers
     */
    public String getPassword(Long userId)
    {
        return userRepo.getPassword(userId);
    }

    /**
     * @param id
     *            Benutzer-ID
     * @return Den zur ID gehördenen Benutzer
     */
    public UserDto getUserById(Long id)
    {
        try
        {
            User user = userRepo.findOne(id);

            return (user != null) ? userMapper.mapToDto(user) : null;

        }
        catch (final Exception e)
        {
            logger.error(e);
        }

        return null;
    }

    /**
     * @param login
     *            Login des Benutzers
     * @return Den zum Login gehörenden Benutzer
     */
    public UserDto getUserByLogin(String login)
    {
        try
        {
            User user = userRepo.findUserByLogin(login);

            return (user != null) ? userMapper.mapToDto(user) : null;

        }
        catch (final Exception e)
        {
            logger.error(e);
        }

        return null;
    }

    /**
     * @param login
     *            Benutzer-Login
     * @param password
     *            Durch Javascript einfach codiertes Passwort
     * @return true, wenn Benutzer bekannt, Passwort stimmt, Benutzer aktiviert ist, Benutzer mindestens einer
     *         Gruppe zugeordnet ist, der Standardmandant des Benutzers aktiviert ist... sonst false
     */
    public boolean isUserAuthenticated(String login, String password, boolean checkLdap)
            throws AuthenticationException
    {
        User persistentUser = userRepo.findUserByLogin(login);

        String finalPw = PasswordEncoder.encodeComplex(password, Long.toString(persistentUser.getId()));
        if (!persistentUser.isAccountLocked())
        {
            if (persistentUser.isEnabled() && !persistentUser.getEnabledGroups().isEmpty()
                    && persistentUser.getDefaultClient().isEnabled())
            {
                if (!checkLdap)
                {
                    if (finalPw.equalsIgnoreCase(persistentUser.getPassword()))
                    {
                        // Anzahl der Fehlversuche zurücksetzen

                        persistentUser.setLoginAttempts(0);
                        userRepo.save(persistentUser);
                        logger.info("DB-LOGIN: user=" + login);

                        return true;
                    }
                    else
                    {
                        // Anzahl der Fehlversuche registrieren
                        boolean locked = checkLoginAttempts(persistentUser);

                        if (locked)
                        {
                            logger.info("DB-ACCOUNT-LOCKED: to many retries for user=" + login);
                            throw new LockedException("");
                        }
                        else
                        {
                            throw new BadCredentialsException("");
                        }
                    }
                }
                else
                {
                    LdapTemplate ldapTemplate = ldapConfigService.setupLdapConnection(ldapConfigService
                            .getLdapConfig());
                    LdapConfigDto ldapConfig = ldapConfigService.getLdapConfig();

                    Object[] filterParams =
                    { login };
                    Filter filter = LdapQueryBuilder.query().filter(ldapConfig.getFilter(), filterParams)
                            .filter();

                    LdapName

                    base = LdapUtils.newLdapName(ldapConfig.getBase());

                    if (ldapTemplate.authenticate(base, filter.encode(), password))
                    {
                        // Anzahl der Fehlversuche zurücksetzen

                        persistentUser.setLoginAttempts(0);
                        userRepo.save(persistentUser);
                        logger.info("LDAP-LOGIN: user=" + login);
                        return true;
                    }
                    else
                    {
                        // Anzahl der Fehlversuche registrieren
                        boolean locked = checkLoginAttempts(persistentUser);

                        if (locked)
                        {
                            logger.info("LDAP-ACCOUNT-LOCKED: to many retries for user=" + login);
                            throw new LockedException("");
                        }
                        else
                        {
                            throw new BadCredentialsException("");
                        }
                    }
                }
            }
            else
            {
                throw new DisabledException("");
            }
        }
        else
        {
            throw new LockedException("");
        }
    }

    /**
     * Speichert einen Benutzer.
     * 
     * @param detachedUser
     *            Zu speichernder Benutzer
     */
    @Transactional
    public void saveUser(UserDto detachedUser)
    {
        try
        {
            User draftUser = userMapper.mapToEntity(detachedUser);
            User persistedUser = userRepo.save(draftUser);

            if (detachedUser.getId() > -1)
            {
                // Passwort wurde bei einem schon persistenten Benutzer
                // geändert.
                if (detachedUser.getPassword() != null && !detachedUser.getPassword().isEmpty())
                {
                    persistedUser.setPassword(PasswordEncoder.encodeComplex(detachedUser.getPassword(),
                            Long.toString(persistedUser.getId())));
                    persistedUser.setPasswordChangedTimeStamp(new Date());
                }
            }
            else
            {
                // Wurde kein Passwort vor der ersten Übertragung
                // gesetzt, wird es hier generiert.
                String pw = detachedUser.getPassword();
                if (pw == null || pw.isEmpty())
                {
                    pw = env.getRequiredProperty(PROPERTY_NAME_INIT_USER_PASSWORD);
                }
                persistedUser.setPassword(PasswordEncoder.encodeComplex(pw,
                        Long.toString(persistedUser.getId())));
                persistedUser.setPasswordChangedTimeStamp(new Date());
            }

            userRepo.save(persistedUser);
        }
        catch (final Exception e)
        {
            logger.error(e);
        }

    }

    /**
     * Speichert eine Liste von Benutzern.
     * 
     * @param detachedUsers
     *            Zu speichernde Benutzer
     */
    public void saveUsers(List<UserDto> detachedUsers)
    {
        try
        {
            detachedUsers.forEach(e -> saveUser(e));
        }
        catch (final Exception e)
        {
            logger.error(e);
        }
    }

    private boolean checkLoginAttempts(User persistentUser)
    {
        persistentUser.setLoginAttempts(persistentUser.getLoginAttempts() + 1);

        if (environmentService.getEnvironment().getAllowedLoginAttempts() <= persistentUser
                .getLoginAttempts())
        {
            persistentUser.setAccountLocked(true);
        }

        userRepo.save(persistentUser);

        return persistentUser.isAccountLocked();
    }
}
