package com.ecg.webclient.feature.nomination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ecg.webclient.feature.administration.authentication.AuthenticationUtil;
import com.ecg.webclient.feature.administration.viewmodell.FeatureDto;

@Scope("request")
@Controller
@RequestMapping(value = "/nom")
public class NominationController
{
	@Autowired
	private AuthenticationUtil authUtil;

	@RequestMapping(method = RequestMethod.GET)
	public String load()
	{
		return getLoadingRedirectTemplate();
	}

	@RequestMapping(method = RequestMethod.POST)
	public String load(@ModelAttribute("currentFeature") FeatureDto feature)
	{
		authUtil.setSelectedFeature(feature);
		return getLoadingRedirectTemplate();
	}

	protected String getLoadingRedirectTemplate()
	{
		return "nomination";
	}

}
