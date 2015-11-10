package com.ecg.webclient.feature.administration.authentication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.administration.service.GroupService;
import com.ecg.webclient.feature.administration.service.LdapConfigService;
import com.ecg.webclient.feature.administration.service.RoleService;
import com.ecg.webclient.feature.administration.service.UserService;
import com.ecg.webclient.feature.administration.viewmodell.ClientDto;
import com.ecg.webclient.feature.administration.viewmodell.GroupDto;
import com.ecg.webclient.feature.administration.viewmodell.RoleDto;
import com.ecg.webclient.feature.administration.viewmodell.UserDto;

@Component("dbAuthenticationProvider")
public class DbAuthenticationProvider implements AuthenticationProvider
{
	@Autowired
	UserService			userService;
	@Autowired
	GroupService		groupService;
	@Autowired
	RoleService			roleService;
	@Autowired
	LdapConfigService	ldapConfigService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException
	{
		String login = authentication.getName();
		String password = authentication.getCredentials().toString();

		UserDto persistentUser = userService.getUserByLogin(login);

		if (persistentUser == null)
		{
			throw new BadCredentialsException("");
		}
		else
		{
			boolean isAuthenticated = false;
			// Ldap Authentifizierung
			if (!persistentUser.isInternal())
			{
				if (ldapConfigService.getLdapConfig().isEnabled())
				{
					isAuthenticated = userService.isUserAuthenticated(login, password, true);
				}
				else
				{
					throw new InsufficientAuthenticationException("");
				}
			}
			// DB Authentifizierung
			else
			{
				isAuthenticated = userService.isUserAuthenticated(login, password, false);
			}

			if (isAuthenticated)
			{
				UserDto user = userService.getUserByLogin(login);
				ClientDto defaultClient = userService.getDefaultClientForUser(user);

				List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
				// zugeordnete Rollen für den Mandanten setzen, welche
				// selbst aktiv
				// sind und deren Feature aktiv ist
				for (GroupDto group : groupService.getEnabledGroupsForIds(user.getGroupIdObjects()))
				{
					if (groupService.getClientForGroupId(group.getId()).getId() == defaultClient.getId())
					{
						for (RoleDto role : roleService
								.getEnabledRolesWithEnabledFeatureForIds(group.getRoleIdObjects()))
						{
							DbGrantedAuthoritiy newAuth = new DbGrantedAuthoritiy(role.getCombinedName());
							grantedAuths.add(newAuth);
						}
					}
				}

				Authentication auth = new UsernamePasswordAuthenticationToken(login, password, grantedAuths);

				return auth;
			}
		}

		return null;
	}

	@Override
	public boolean supports(Class<?> authentication)
	{
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
