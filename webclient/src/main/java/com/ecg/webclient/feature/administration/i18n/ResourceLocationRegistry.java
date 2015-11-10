package com.ecg.webclient.feature.administration.i18n;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.i18n.WebClientResourceLocation;

@Component
public class ResourceLocationRegistry
{
	static final Logger logger = LogManager.getLogger(ResourceLocationRegistry.class.getName());

	List<WebClientResourceLocation> resourceLocations;

	@Autowired
	public ResourceLocationRegistry(List<WebClientResourceLocation> resourceLocationsToRegister)
	{
		resourceLocations = new ArrayList<WebClientResourceLocation>();

		for (WebClientResourceLocation resourceLocation : resourceLocationsToRegister)
		{
			if (resourceLocations.contains(resourceLocation))
			{
				logger.error("i18n resource location with name " + resourceLocation.getMessageBaseName()
						+ " is already registered. Features with same i18n resource file names are existing. Fix that.");
			}
			else
			{
				resourceLocations.add(resourceLocation);
			}
		}
	}

	public List<WebClientResourceLocation> getResourceLocations()
	{
		return resourceLocations;
	}
}
