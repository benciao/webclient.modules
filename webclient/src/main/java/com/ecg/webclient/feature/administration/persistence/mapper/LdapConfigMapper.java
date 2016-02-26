package com.ecg.webclient.feature.administration.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.authentication.PasswordEncoder;
import com.ecg.webclient.feature.administration.persistence.modell.LdapConfig;
import com.ecg.webclient.feature.administration.persistence.repo.LdapConfigRepository;
import com.ecg.webclient.feature.administration.viewmodell.LdapConfigDto;

/**
 * Mapped die Eigenschaften einer der Persistenz bekannten Entität auf eine
 * detachte LDAP-Konfiguration oder umgekehrt.
 * 
 * @author arndtmar
 */
@Component
public class LdapConfigMapper
{
	private LdapConfigRepository ldapConfigRepo;

	@Autowired
	public LdapConfigMapper(LdapConfigRepository ldapConfigRepo)
	{
		this.ldapConfigRepo = ldapConfigRepo;
	}

	/**
	 * Wandelt eine attachte LDAP-Konfiguration in eine detachte um.
	 * 
	 * @param ldapConfig
	 *            attachte LDAP-Konfiguration
	 * @return Detachete LDAP-Konfiguration
	 */
	public LdapConfigDto mapToDto(LdapConfig ldapConfig)
	{
		LdapConfigDto dto = new LdapConfigDto();
		dto.setId(ldapConfig.getId());
		dto.setDelete(false);
		dto.setEnabled(ldapConfig.isEnabled());
		dto.setUrl(ldapConfig.getUrl());
		dto.setUsername(ldapConfig.getUsername());
		dto.setPassword(PasswordEncoder.decode2Way(ldapConfig.getPassword()));
		dto.setBase(ldapConfig.getBase());
		dto.setFilter(ldapConfig.getFilter());

		return dto;
	}

	/**
	 * Wandelt eine Liste von attachten LDAP-Konfigurationen in eine Liste von
	 * detachten LDAP-Konfigurationen um.
	 * 
	 * @param ldapConfigs
	 *            Liste von attachten LDAP-Konfigurationen
	 * @return Liste von detachten LDAP-Konfigurationen
	 */
	public List<LdapConfigDto> mapToDtos(List<LdapConfig> ldapConfigs)
	{
		List<LdapConfigDto> result = new AutoPopulatingList<LdapConfigDto>(LdapConfigDto.class);

		ldapConfigs.forEach(e -> result.add(mapToDto(e)));

		return result;
	}

	/**
	 * Wandelt eine Liste von detachten LDAP-Konfigurationen in eine Liste von
	 * attachten LDAP-Konfigurationen um.
	 * 
	 * @param dtos
	 *            Liste von detachten LDAP-Konfigurationen
	 * @return Liste von attachten LDAP-Konfigurationen
	 */
	public List<LdapConfig> mapToEntities(List<LdapConfigDto> dtos)
	{
		List<LdapConfig> result = new ArrayList<LdapConfig>();

		dtos.forEach(e -> result.add(mapToEntity(e)));

		return result;
	}

	/**
	 * Wandelt eine detachte LDAP-Konfiguration in eine attachte um.
	 * 
	 * @param dto
	 *            Detachte LDAP-Konfiguration
	 * @return attachte LDAP-Konfiguration
	 */
	public LdapConfig mapToEntity(LdapConfigDto dto)
	{
		LdapConfig ldapConfig = new LdapConfig();
		ldapConfig.setId(dto.getId());
		ldapConfig.setEnabled(dto.isEnabled());
		ldapConfig.setUrl(dto.getUrl());
		ldapConfig.setUsername(dto.getUsername());
		ldapConfig.setPassword(PasswordEncoder.encode2Way(dto.getPassword()));
		ldapConfig.setBase(dto.getBase());
		ldapConfig.setFilter(dto.getFilter());

		LdapConfig persistentLdapConfig = ldapConfigRepo.findOne(ldapConfig.getId());
		if (persistentLdapConfig != null)
		{
			return persistentLdapConfig.bind(ldapConfig);
		}

		return ldapConfig;
	}
}
