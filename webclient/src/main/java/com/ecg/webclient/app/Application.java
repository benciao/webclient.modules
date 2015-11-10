package com.ecg.webclient.app;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.ecg.webclient.feature.administration.i18n.ResourceLocationRegistry;
import com.ecg.webclient.feature.i18n.WebClientResourceLocation;

@Configuration
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan("com.ecg")
public class Application
{
	@Autowired
	private ResourceLocationRegistry resourceLocationRegistry;

	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public MessageSource messageSource()
	{
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

		List<String> baseNames = new ArrayList<String>();

		for (WebClientResourceLocation resourceLocation : resourceLocationRegistry.getResourceLocations())
		{
			baseNames.add(resourceLocation.getMessageBaseName());
		}

		String[] array = baseNames.toArray(new String[baseNames.size()]);
		messageSource.setBasenames(array);
		messageSource.setDefaultEncoding("ISO-8859-1");
		return messageSource;
	}
}
