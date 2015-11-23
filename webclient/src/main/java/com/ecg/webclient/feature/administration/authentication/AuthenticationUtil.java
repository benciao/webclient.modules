package com.ecg.webclient.feature.administration.authentication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.ecg.webclient.feature.administration.service.ClientService;
import com.ecg.webclient.feature.administration.service.GroupService;
import com.ecg.webclient.feature.administration.service.RoleService;
import com.ecg.webclient.feature.administration.service.UserService;
import com.ecg.webclient.feature.administration.setup.AdministrationFeature;
import com.ecg.webclient.feature.administration.setup.SecurityAdminAccessRole;
import com.ecg.webclient.feature.administration.setup.SetupSystemAccessRole;
import com.ecg.webclient.feature.administration.viewmodell.ClientDto;
import com.ecg.webclient.feature.administration.viewmodell.FeatureDto;
import com.ecg.webclient.feature.administration.viewmodell.GroupDto;
import com.ecg.webclient.feature.administration.viewmodell.RoleDto;
import com.ecg.webclient.feature.administration.viewmodell.UserDto;

@Scope("session")
@Component
public class AuthenticationUtil
{
    private List<ClientDto>         clients;
    private ClientDto               selectedClient;
    private FeatureDto              selectedFeature;
    private ClientService           clientService;
    private UserService             userService;
    private GroupService            groupService;
    private RoleService             roleService;
    private SessionLocaleResolver   sessionLocaleResolver;

    private AdministrationFeature   adminFeature    = new AdministrationFeature();
    private SecurityAdminAccessRole secAdminRole    = new SecurityAdminAccessRole(adminFeature);
    private SetupSystemAccessRole   setupSystemRole = new SetupSystemAccessRole(adminFeature);

    @Autowired
    public AuthenticationUtil(ClientService clientService, UserService userService,
            GroupService groupService, RoleService roleService, LocaleResolver localeResolver)

    {
        this.clientService = clientService;
        this.userService = userService;
        this.groupService = groupService;
        this.roleService = roleService;
        this.sessionLocaleResolver = (SessionLocaleResolver) localeResolver;
        initSelectedClient();
    }

    @Transactional
    public void changeUserPassword(String simpleEncodedPw)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();

        UserDto user = userService.getUserByLogin(login);
        // Pw wird im Service ein 2. Mal encoded
        user.setPassword(simpleEncodedPw);
        user.setChangePasswordOnNextLogin(false);
        userService.saveUser(user);

        // da Service kein neues PW zurückliefert, wird hier für das Token
        // nochmal verschlüsselt
        String password = PasswordEncoder.encodeComplex(simpleEncodedPw, Long.toString(user.getId()));

        assignRolesToUserSession(user, auth, login, password);
    }

    public String getClientChangePath()
    {
        return "/changeClient";
    }

    public List<ClientDto> getClients()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();

        UserDto user = userService.getUserByLogin(login);

        clients = new ArrayList<ClientDto>();

        if (auth.getAuthorities().contains(new DbGrantedAuthoritiy("ROLE_" + secAdminRole.getName()))
                || auth.getAuthorities().contains(
                        new DbGrantedAuthoritiy("ROLE_" + setupSystemRole.getName())))
        {
            clients = clientService.getAllClients(true);
        }
        else
        {
            for (ClientDto client : clientService.getAssignedClientsForGroups(user.getGroupIdObjects()))
            {
                if (client.isEnabled())
                {
                    clients.add(client);
                }
            }
        }

        if (selectedClient == null)
        {
            initSelectedClient();
        }

        boolean selectedClientNotIncluded = true;
        for (ClientDto clientDto : clients)
        {
            if (clientDto.equals(selectedClient))
            {
                selectedClientNotIncluded = false;
                break;
            }
        }

        if (selectedClientNotIncluded)
        {
            setSelectedClient(clients.get(0));
        }

        return clients;
    }

    public UserDto getCurrentUser()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();

        UserDto user = userService.getUserByLogin(login);

        return user;
    }

    public ClientDto getSelectedClient()
    {
        if (selectedClient != null)
        {
            selectedClient = clientService.getClient(selectedClient.getId());
        }
        return selectedClient;
    }

    public FeatureDto getSelectedFeature()
    {
        return selectedFeature;
    }

    public SessionLocaleResolver getSessionLocaleResolver()
    {
        return sessionLocaleResolver;
    }

    public void loginAsUser(Long userId)
    {
        UserDto user = userService.getUserById(userId);
        String password = userService.getPassword(userId);

        this.selectedClient = clientService.getClient(Long.parseLong(user.getDefaultClient()));

        List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
        // zugeordnete Rollen für den Mandanten setzen
        for (GroupDto group : groupService.getEnabledGroupsForIds(user.getGroupIdObjects()))
        {
            if (groupService.getClientForGroupId(group.getId()).getId() == selectedClient.getId())
            {
                for (RoleDto role : roleService.getEnabledRolesWithEnabledFeatureForIds(group
                        .getRoleIdObjects()))
                {
                    DbGrantedAuthoritiy newAuth = new DbGrantedAuthoritiy(role.getCombinedName());
                    grantedAuths.add(newAuth);
                }
            }
        }

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user.getLogin(), password, grantedAuths));

    }

    public void setNewAuthority()
    {        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        String password = auth.getCredentials().toString();

        UserDto user = userService.getUserByLogin(login);

        assignRolesToUserSession(user, auth, login, password);
    }

    public void setSelectedClient(ClientDto selectedClient)
    {
        this.selectedClient = selectedClient;
    }
    
    public void setSelectedClientWithNewAuthority(ClientDto selectedClient)
    {
        this.selectedClient = selectedClient;
        this.setNewAuthority();
    }

    public void setSelectedFeature(FeatureDto selectedFeature)
    {
        this.selectedFeature = selectedFeature;
    }

    private void assignRolesToUserSession(UserDto user, Authentication auth, String login, String password)
    {
        List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
        // zugeordnete Rollen für den Mandanten setzen, welche selbst aktiv
        // sind und deren Feature aktiv ist
        for (GroupDto group : groupService.getEnabledGroupsForIds(user.getGroupIdObjects()))
        {
            // Nutzer mit dieser Rolle erhält alle ihm zugeordneten Rollen über
            // alle Mandanten
            if (auth.getAuthorities().contains(new DbGrantedAuthoritiy("ROLE_" + secAdminRole.getName()))
                    || auth.getAuthorities().contains(
                            new DbGrantedAuthoritiy("ROLE_" + setupSystemRole.getName())))
            {
                for (RoleDto role : roleService.getEnabledRolesWithEnabledFeatureForIds(group
                        .getRoleIdObjects()))
                {
                    DbGrantedAuthoritiy newAuth = new DbGrantedAuthoritiy(role.getCombinedName());
                    grantedAuths.add(newAuth);
                }
            }
            // jeder andere Nutzer erhält nur die ihm zugeordneten Rollen des
            // gerade ausgewählten Mandanten
            else
            {
                if (groupService.getClientForGroupId(group.getId()).getId() == selectedClient.getId())
                {
                    for (RoleDto role : roleService.getRolesForIds(group.getRoleIdObjects()))
                    {
                        DbGrantedAuthoritiy newAuth = new DbGrantedAuthoritiy(role.getCombinedName());
                        grantedAuths.add(newAuth);
                    }
                }
            }
        }

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(login, password, grantedAuths));
    }

    private void initSelectedClient()
    {
        List<ClientDto> clients = clientService.getAllClients(true);
        if (!clients.isEmpty())
        {
            this.selectedClient = clients.get(0);
        }
    }
}
