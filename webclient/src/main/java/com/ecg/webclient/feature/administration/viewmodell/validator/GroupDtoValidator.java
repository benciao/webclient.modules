package com.ecg.webclient.feature.administration.viewmodell.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ecg.webclient.feature.administration.service.GroupService;
import com.ecg.webclient.feature.administration.viewmodell.GroupConfig;
import com.ecg.webclient.feature.administration.viewmodell.GroupDto;

/**
 * Dieser Validator prüft die Eingaben für ein Objekt vom Typ {@link GroupDto}.
 * 
 * @author arndtmar
 */
@Component
public class GroupDtoValidator implements Validator
{
	private GroupService groupService;

	@Autowired
	public GroupDtoValidator(GroupService groupService)
	{
		this.groupService = groupService;
	}

	@Override
	public boolean supports(Class<?> clazz)
	{
		return GroupConfig.class.equals(clazz);
	}

	@Override
	public void validate(Object object, Errors errors)
	{
		if (((GroupConfig) object).isDoCopy())
		{
			GroupDto copyGroup = ((GroupConfig) object).getCopyGroup();

			GroupDto result = groupService.getGroupByNameForCurrentClient(copyGroup.getName(),
					copyGroup.getClient().getId());

			if (result != null)
			{
				errors.rejectValue("copyGroup.name", "group.rejected.duplicated.name");
			}

			if (copyGroup.getName().isEmpty() || copyGroup.getName().length() < 4 || copyGroup.getName().length() > 100)
			{
				errors.rejectValue("copyGroup.name", "group.rejected.size.name");
			}
			if (copyGroup.getDescription().isEmpty() || copyGroup.getDescription().length() < 1
					|| copyGroup.getDescription().length() > 100)
			{
				errors.rejectValue("copyGroup.description", "group.rejected.size.description");
			}
		}
		else
		{
			List<GroupDto> groups = ((GroupConfig) object).getGroups();

			int counter = 0;
			for (GroupDto group : groups)
			{
				if (group.getId() == -1)
				{
					GroupDto result = groupService.getGroupByNameForCurrentClient(group.getName(),
							((GroupConfig) object).getClientId());

					if (result != null)
					{
						errors.rejectValue("groups[" + counter + "].name", "group.rejected.duplicated.name");
					}
				}

				if (group.getName().isEmpty() || group.getName().length() < 4 || group.getName().length() > 100)
				{
					errors.rejectValue("groups[" + counter + "].name", "group.rejected.size.name");
				}
				if (group.getDescription().isEmpty() || group.getDescription().length() < 1
						|| group.getDescription().length() > 100)
				{
					errors.rejectValue("groups[" + counter + "].description", "group.rejected.size.description");
				}

				counter++;
			}
		}
	}
}
