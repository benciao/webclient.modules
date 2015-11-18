package com.ecg.webclient.feature.administration.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ecg.webclient.feature.administration.service.AuditService;
import com.ecg.webclient.feature.administration.service.UserService;
import com.ecg.webclient.feature.administration.setup.AdministrationFeature;
import com.ecg.webclient.feature.administration.setup.SecurityAdminAccessRole;
import com.ecg.webclient.feature.administration.setup.SetupSystemAccessRole;
import com.ecg.webclient.feature.administration.viewmodell.UserDto;

/**
 * Controller zum Anzeigen von Statistiken.
 * 
 * @author arndtmar
 *
 */
@Scope("request")
@Controller
@RequestMapping(value = "/admin/statistic/global")
public class StatisticsController
{
	@Autowired
	private UserService		userService;
	@Autowired
	private AuditService	auditService;

	/**
	 * Behandelt GET-Requests vom Typ "/admin/statistic/global". Lädt alle
	 * Statistiken.
	 * 
	 * @return Template
	 */
	@PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY + "') OR hasRole('"
			+ AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
	@RequestMapping(method = RequestMethod.GET)
	public String showGlobalStatistics(Model model)
	{
		return getLoadingRedirectTemplate();
	}

	@RequestMapping(value = "/pieData", method = RequestMethod.GET)
	public @ResponseBody List<PieDataPoint> getPieData()
	{
		List<PieDataPoint> result = new ArrayList<PieDataPoint>();

		List<UserDto> users = userService.getAllUsers(false);

		for (UserDto user : users)
		{
			Integer countOk = auditService.countLoginAttempts(user, true);
			Integer countFailed = auditService.countLoginAttempts(user, false);
			result.add(new PieDataPoint(user.getLogin(), countOk, countFailed));
		}

		return result;
	}

	@RequestMapping(value = "/scatterDataOk", method = RequestMethod.GET)
	public @ResponseBody List<ScatterDataPoint> getScatterDataOk()
	{
		List<ScatterDataPoint> result = new ArrayList<ScatterDataPoint>();

		List<UserDto> users = userService.getAllUsers(false);

		Calendar cal = Calendar.getInstance();
		for (UserDto user : users)
		{
			for (Date date : auditService.getLoginAttemptsTime(user, true))
			{
				cal.setTime(date);
				Integer hour =  cal.get(Calendar.HOUR_OF_DAY);
				Integer minute = cal.get(Calendar.MINUTE);
				String time = hour + "." + (minute/60);
				result.add(new ScatterDataPoint(user.getLogin(), Double.parseDouble(time)));	
			}
			
		}

		return result;
	}
	
	@RequestMapping(value = "/scatterDataFailed", method = RequestMethod.GET)
	public @ResponseBody List<ScatterDataPoint> getScatterDataFailed()
	{
		List<ScatterDataPoint> result = new ArrayList<ScatterDataPoint>();

		List<UserDto> users = userService.getAllUsers(false);

		Calendar cal = Calendar.getInstance();
		for (UserDto user : users)
		{
			for (Date date : auditService.getLoginAttemptsTime(user, false))
			{
				cal.setTime(date);
				Integer hour =  cal.get(Calendar.HOUR_OF_DAY);
				Integer minute = cal.get(Calendar.MINUTE);
				String time = hour + "." + (minute/60);
				result.add(new ScatterDataPoint(user.getLogin(), Double.parseDouble(time)));	
			}
			
		}

		return result;
	}

	protected String getLoadingRedirectTemplate()
	{
		return "feature/administration/loginAttemptsGlobal";
	}

	public class ScatterDataPoint
	{
		private String	name;
		private double	value;

		public ScatterDataPoint(String name, double value)
		{
			this.name = name;
			this.value = value;
		}

		public String getName()
		{
			return name;
		}

		public double getValue()
		{
			return value;
		}
	}

	public class PieDataPoint
	{
		private String	name;
		private int		countOk;
		private int		countFailed;

		public PieDataPoint(String name, int countOk, int countFailed)
		{
			this.name = name;
			this.countOk = countOk;
			this.countFailed = countFailed;
		}

		public String getName()
		{
			return name;
		}

		public int getCountOk()
		{
			return countOk;
		}

		public int getCountFailed()
		{
			return countFailed;
		}
	}
}
