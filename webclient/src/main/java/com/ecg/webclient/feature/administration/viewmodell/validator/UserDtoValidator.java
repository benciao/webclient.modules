package com.ecg.webclient.feature.administration.viewmodell.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ecg.webclient.feature.administration.service.UserService;
import com.ecg.webclient.feature.administration.viewmodell.UserConfig;
import com.ecg.webclient.feature.administration.viewmodell.UserDto;

/**
 * Dieser Validator prüft die Eingaben für ein Objekt vom Typ {@link UserDto}.
 * 
 * @author arndtmar
 */
@Component
public class UserDtoValidator implements Validator
{
	private UserService userService;

	@Autowired
	public UserDtoValidator(UserService userService)
	{
		this.userService = userService;
	}

	@Override
	public boolean supports(Class<?> clazz)
	{
		return UserConfig.class.equals(clazz);
	}

	@Override
	public void validate(Object object, Errors errors)
	{
		List<UserDto> users = ((UserConfig) object).getUsers();

		int counter = 0;
		for (UserDto user : users)
		{
			if (user.getId() == -1)
			{
				UserDto result = userService.getUserByLogin(user.getLogin());

				if (result != null)
				{
					errors.rejectValue("users[" + counter + "].login", "user.rejected.duplicated.login");
				}
			}

			if (user.getLogin().isEmpty() || user.getLogin().length() < 5 || user.getLogin().length() > 20)
			{
				errors.rejectValue("users[" + counter + "].login", "user.rejected.size.login");
			}
			if (user.getFirstname().isEmpty() || user.getFirstname().length() < 3 || user.getFirstname().length() > 30)
			{
				errors.rejectValue("users[" + counter + "].firstname", "user.rejected.size.firstname");
			}
			if (user.getLastname().isEmpty() || user.getLastname().length() < 2 || user.getLastname().length() > 50)
			{
				errors.rejectValue("users[" + counter + "].lastname", "user.rejected.size.lastname");
			}
			if (!user.getEmail().isEmpty() && !user.getEmail().contains("@"))
			{
				errors.rejectValue("users[" + counter + "].email", "user.rejected.size.email");
			}

			counter++;
		}
	}
}
