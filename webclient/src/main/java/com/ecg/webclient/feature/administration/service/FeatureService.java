package com.ecg.webclient.feature.administration.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.persistence.mapper.FeatureMapper;
import com.ecg.webclient.feature.administration.persistence.modell.Feature;
import com.ecg.webclient.feature.administration.persistence.repo.FeatureRepository;
import com.ecg.webclient.feature.administration.viewmodell.FeatureDto;
import com.ecg.webclient.feature.authorisation.WebClientFeature;

/**
 * Service zum Bearbeiten von Features.
 * 
 * @author arndtmar
 */
@Component
public class FeatureService
{
	static final Logger logger = LogManager.getLogger(FeatureService.class.getName());

	FeatureRepository		featureRepo;
	FeatureMapper			featureMapper;
	List<WebClientFeature>	featuresToRegister;

	@Autowired
	public FeatureService(FeatureRepository featureRepo, FeatureMapper featureMapper,
			List<WebClientFeature> featuresToRegister)
	{
		this.featureRepo = featureRepo;
		this.featureMapper = featureMapper;

		registerFeatures(featuresToRegister);
	}

	/**
	 * @param onlyEnabled
	 *            true, wenn nur die aktiven Features geladen werden sollen,
	 *            sonst false
	 * @param autorizationRequired
	 *            true, wenn nur für den Benutzer autorisierte Features
	 *            zurückgeliefert werden soll, sonst false
	 * @return Alle im System verfügbaren Features wenn false, sonst nur die
	 *         aktivierten. Darüber hinaus werden immer nur die Features
	 *         zurückgegeben, für welche der Benutzter berechtigt ist.
	 */
	public List<FeatureDto> getAllFeatures(boolean onlyEnabled, boolean autorizationRequired)
	{
		List<Feature> attachedFeatures = new ArrayList<Feature>();

		try
		{
			if (!onlyEnabled)
			{
				featureRepo.findAll().forEach(e -> attachedFeatures.add(e));
			}
			else
			{
				featureRepo.findAllEnabled(true).forEach(e -> attachedFeatures.add(e));
			}
		}
		catch (final Exception e)
		{
			logger.error(e);
		}

		AutoPopulatingList<FeatureDto> result = new AutoPopulatingList<FeatureDto>(FeatureDto.class);

		for (Feature attachedFeature : attachedFeatures)
		{
			if (autorizationRequired)
			{
				if (isFeatureAutorized(attachedFeature))
				{
					result.add(featureMapper.mapToDto(attachedFeature));
				}
			}
			else
			{
				result.add(featureMapper.mapToDto(attachedFeature));
			}
		}

		return result;
	}

	/**
	 * @param id
	 *            Id des Features
	 * @return das zur Id gehörende Feature
	 */
	public FeatureDto getFeatureById(long id)
	{
		try
		{
			return featureMapper.mapToDto(featureRepo.findOne(id));
		}
		catch (final Exception e)
		{
			logger.error(e);
		}
		
		return null;
	}

	/**
	 * Speichert das zu übergebende Feature.
	 * 
	 * @param detachedFeature
	 *            das zu speichernde Feature
	 * @return Das gespeicherte Feature
	 */
	public FeatureDto saveFeature(FeatureDto detachedFeature)
	{
		try
		{
			Feature draftFeature = featureMapper.mapToEntity(detachedFeature);
			Feature persistedFeature = featureRepo.save(draftFeature);

			if (persistedFeature != null)
			{
				return featureMapper.mapToDto(persistedFeature);
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

	/**
	 * Speichert die in der Liste enthaltenen Features.
	 * 
	 * @param detachedFeatures
	 *            Liste mit zu speichernden Features
	 */
	public void saveFeatures(List<FeatureDto> detachedFeatures)
	{
		try
		{
			detachedFeatures.forEach(e -> saveFeature(e));
		}
		catch (final Exception e)
		{
			logger.error(e);
		}
	}

	private boolean isFeatureAutorized(Feature feature)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		for (GrantedAuthority ga : auth.getAuthorities())
		{
			if (ga.getAuthority().startsWith(feature.getName()))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * @param featuresToRegister
	 */
	@Transactional
	private void registerFeatures(List<WebClientFeature> featuresToRegister)
	{
		this.featuresToRegister = featuresToRegister;
		for (WebClientFeature feature : featuresToRegister)
		{
			Feature lookupFeature = featureRepo.findFeatureByName(feature.getFeatureKey());

			if (lookupFeature == null)
			{
				Feature newFeature = new Feature();
				newFeature.setName(feature.getFeatureKey());
				newFeature.setDeactivatable(feature.isDeactivatable());
				newFeature.setEnabled(true);
				newFeature.setIconPath(feature.getIconPath());
				newFeature.setLink(feature.getLink());
				newFeature.setI18nVariable(feature.getI18nVariable());
				newFeature.setLoadingRedirectTemplate(feature.getLoadingRedirectTemplate());

				featureRepo.save(newFeature);
			}
			else
			{
				logger.error(
						"feature with name " + feature.getFeatureKey() + " already existing. registration was skiped.");
			}
		}
	}
}
