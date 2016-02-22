package com.ecg.webclient.feature.administration.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AutoPopulatingList;

import com.ecg.webclient.feature.administration.persistence.modell.Feature;
import com.ecg.webclient.feature.administration.persistence.repo.FeatureRepository;
import com.ecg.webclient.feature.administration.viewmodell.FeatureDto;

/**
 * Mapped die Eigenschaften einer der Persistenz bekannten Entität auf ein
 * detachtes Feature oder umgekehrt.
 * 
 * @author arndtmar
 */
@Component
public class FeatureMapper
{
	private FeatureRepository featureRepo;

	@Autowired
	public FeatureMapper(FeatureRepository featureRepo)
	{
		this.featureRepo = featureRepo;
	}

	/**
	 * Wandelt ein attachtes Feature in ein detachtes um.
	 * 
	 * @param attachedFeature
	 *            attachtes Feature
	 * @return Detachete Rolle
	 */
	public FeatureDto mapToDto(Feature attachedFeature)
	{
		FeatureDto detachedFeature = new FeatureDto();
		detachedFeature.setName(attachedFeature.getName());
		detachedFeature.setEnabled(attachedFeature.isEnabled());
		detachedFeature.setId(attachedFeature.getId());
		detachedFeature.setDeactivatable(attachedFeature.isDeactivatable());
		detachedFeature.setIconPath(attachedFeature.getIconPath());
		detachedFeature.setLink(attachedFeature.getLink());
		detachedFeature.setI18nVariable(attachedFeature.getI18nVariable());
		detachedFeature.setLoadingRedirectTemplate(attachedFeature.getLoadingRedirectTemplate());

		return detachedFeature;
	}

	/**
	 * Wandelt eine Liste von attacheten Features in eine Liste von detachten
	 * Features um.
	 * 
	 * @param attachedFeatures
	 *            Liste von attacheten Features
	 * @return Liste von detachten Features
	 */
	public List<FeatureDto> mapToDtos(List<Feature> attachedFeatures)
	{
		List<FeatureDto> result = new AutoPopulatingList<FeatureDto>(FeatureDto.class);

		attachedFeatures.forEach(e -> result.add(mapToDto(e)));

		return result;
	}

	/**
	 * Wandelt eine Liste von detachten Features in eine Liste von attachten
	 * Features um.
	 * 
	 * @param detachedFeatures
	 *            Liste von detachten Features
	 * @return Liste von zum Speichern vorbereiteten Features
	 */
	public List<Feature> mapToEntities(List<FeatureDto> detachedFeatures)
	{
		List<Feature> result = new ArrayList<Feature>();

		detachedFeatures.forEach(e -> result.add(mapToEntity(e)));

		return result;
	}

	/**
	 * Wandelt ein detachtes Feature in eine attachtes um.
	 * 
	 * @param detachedFeature
	 *            Detachtes Feature
	 * @return zum Speichern vorbereitetes Feature
	 */
	public Feature mapToEntity(FeatureDto detachedFeature)
	{
		Feature entity = new Feature();
		entity.setId(detachedFeature.getId());
		entity.setName(detachedFeature.getName());
		entity.setEnabled(detachedFeature.isEnabled());
		entity.setDeactivatable(detachedFeature.isDeactivatable());
		entity.setIconPath(detachedFeature.getIconPath());
		entity.setLink(detachedFeature.getLink());
		entity.setI18nVariable(detachedFeature.getI18nVariable());
		entity.setLoadingRedirectTemplate(detachedFeature.getLoadingRedirectTemplate());

		Feature persistentFeature = featureRepo.findOne(entity.getId());
		if (persistentFeature != null)
		{
			return persistentFeature.bind(entity);
		}

		return entity;
	}
}
