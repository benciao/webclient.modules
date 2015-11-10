package com.ecg.webclient.feature.administration.viewmodell.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ecg.webclient.feature.administration.viewmodell.ClientProperties;
import com.ecg.webclient.feature.administration.viewmodell.ClientPropertyDto;

/**
 * Dieser Validator prüft die Eingaben für ein Objekt vom Typ
 * {@link ClientPropertyDto}.
 * 
 * @author arndtmar
 */
@Component
public class ClientPropertyDtoValidator implements Validator
{
	@Override
	public boolean supports(Class<?> clazz)
	{
		return ClientProperties.class.equals(clazz);
	}

	@Override
	public void validate(Object object, Errors errors)
	{
		List<ClientPropertyDto> properties = ((ClientProperties) object).getProperties();

		int counter = 0;
		for (ClientPropertyDto property : properties)
		{
            if (propertyKeyExists(properties, property))
			{
				errors.rejectValue("properties[" + counter + "].key", "property.rejected.duplicated.key");
			}

			if (property.getKey().isEmpty() || property.getKey().length() < 4 || property.getKey().length() > 30)
			{
				errors.rejectValue("properties[" + counter + "].key", "property.rejected.size.key");
			}
			if (property.getValue().isEmpty())
			{
				errors.rejectValue("properties[" + counter + "].value", "property.rejected.size.value");
			}

			counter++;
		}
	}

	private boolean propertyKeyExists(List<ClientPropertyDto> persistedProperties, ClientPropertyDto property)
	{
		for (ClientPropertyDto persistedProperty : persistedProperties)
		{
            if (persistedProperty.getKey().equals(property.getKey())
                    && persistedProperty.getId() != property.getId())
			{
				return true;
			}
		}

		return false;
	}
}
