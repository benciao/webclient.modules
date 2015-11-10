package com.ecg.webclient.app;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.ecg.webclient.feature.administration.authentication.RemoteLoginInterceptor;
import com.ecg.webclient.feature.administration.viewmodell.formatter.CustomDateFormatter;

@Configuration
@EnableWebMvc
@Import(
{ DatabaseConfiguration.class })
@PropertySource("classpath:application.properties")
public class WebMvcConfiguration extends WebMvcConfigurerAdapter
{
    @Autowired
    private CustomDateFormatter dateFormatter;

    @Autowired
    private RemoteLoginInterceptor remoteLoginInterceptor;

    @Override
    public void addFormatters(FormatterRegistry registry)
    {
        registry.addFormatter(dateFormatter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(remoteLoginInterceptor).addPathPatterns("/");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("/icons/**").addResourceLocations("classpath:/icons/");
        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/fonts/");
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/img/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/main").setViewName("main");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor()
    {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Bean
    public LocaleResolver localeResolver()
    {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.GERMAN);
        return slr;
    }
}
