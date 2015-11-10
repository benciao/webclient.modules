package com.ecg.weblient.feature.nomination.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller zur Bearbeitung von Requests aus Abwicklungsdialogen.
 * 
 * @author arndtmar
 *
 */
@Scope("request")
@Controller
@RequestMapping(value = "/nom")
public class NominationController
{
	@RequestMapping(method = RequestMethod.GET)
	public String load()
	{
		return getLoadingRedirectTemplate();
	}

	protected String getLoadingRedirectTemplate()
	{
		return "feature/nomination/nomination";
	}

}
