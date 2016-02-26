package com.ecg.webclient.feature.administration.persistence.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.persistence.modell.Audit;
import com.ecg.webclient.feature.administration.persistence.repo.AuditRepository;
import com.ecg.webclient.feature.administration.viewmodell.AuditDto;

/**
 * Mapped einen Audit-Eintrag einer der Persistenz bekannten Entität auf einen
 * detachten Audit-Eintrag oder umgekehrt.
 * 
 * @author arndtmar
 */
@Component
public class AuditMapper
{
	private UserMapper		userMapper;
	private AuditRepository	auditRepo;

	@Autowired
	public AuditMapper(UserMapper userMapper, AuditRepository auditRepo)
	{
		this.userMapper = userMapper;
		this.auditRepo = auditRepo;
	}

	/**
	 * Wandelt einen attachten Audit-Eintrag in einen detachten um.
	 * 
	 * @param audit
	 *            Audit-Eintrag
	 * @return Detacheter Audit-Eintrag
	 */
	public AuditDto mapToDto(Audit audit)
	{
		AuditDto dto = new AuditDto();
		dto.setId(audit.getId());
		dto.setOccurance(audit.getOccurance());
		dto.setAuthenticationOk(audit.isAuthenticationOk());
		dto.setUser(userMapper.mapToDto(audit.getUser()));
		dto.setDelete(false);

		return dto;
	}

	/**
	 * Wandelt eine Liste von attachten Audit-Einträgen in eine Liste von
	 * detachten Audit-Einträgen um.
	 * 
	 * @param audits
	 *            Liste von attachten Audit-Einträgen
	 * @return Liste von detachten Audit-Einträgen
	 */
	public List<AuditDto> mapToDtos(List<Audit> audits)
	{
		List<AuditDto> result = new AutoPopulatingList<AuditDto>(AuditDto.class);

		audits.forEach(e -> result.add(mapToDto(e)));

		return result;
	}

	/**
	 * Wandelt einen detachten Audit-Eintrag in einen attachten um.
	 * 
	 * @param dto
	 *            Detachter Audit-Eintrag
	 * @return attachter Audit-Eintrag
	 */
	public Audit mapToEntity(AuditDto dto)
	{
		Audit audit = new Audit();
		audit.setId(dto.getId());
		audit.setOccurance(dto.getOccurance());
		audit.setAuthenticationOk(dto.isAuthenticationOk());
		audit.setUser(userMapper.mapToEntity(dto.getUser()));

		Audit persistentAudit = auditRepo.findOne(audit.getId());
		if (persistentAudit != null)
		{
			return persistentAudit.bind(audit);
		}

		return audit;
	}
}
