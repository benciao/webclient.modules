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
    @RequestMapping(value = "/contract", method = RequestMethod.GET)
    public String getContract()
    {
        return "feature/nomination/contract";
    }

    @RequestMapping(value = "/storage", method = RequestMethod.GET)
    public String getStorage()
    {
        return "feature/nomination/storage";
    }

    @RequestMapping(value = "/todo", method = RequestMethod.GET)
    public String getToDo()
    {
        return "feature/nomination/todo_monitor";
    }

    @RequestMapping(value = "/todo_ext", method = RequestMethod.GET)
    public String getToDoExt()
    {
        return "feature/nomination/todo_monitor_ext";
    }

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
