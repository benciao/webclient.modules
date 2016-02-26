package com.ecg.webclient.feature.administration.viewmodell.formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class CustomDateFormatter implements Formatter<Date>
{
	private MessageSource messageSource;

	@Autowired
	public CustomDateFormatter(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	@Override
	public Date parse(final String text, final Locale locale) throws ParseException
	{
		final SimpleDateFormat dateFormat = createDateFormat(locale);
		return dateFormat.parse(text);
	}

	@Override
	public String print(final Date object, final Locale locale)
	{
		final SimpleDateFormat dateFormat = createDateFormat(locale);
		return dateFormat.format(object);
	}

	private SimpleDateFormat createDateFormat(final Locale locale)
	{
		final String format = this.messageSource.getMessage("date.format", null, locale);
		final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setLenient(false);
		return dateFormat;
	}
}
