package com.ecg.webclient.feature.administration.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private UserService  userService;
    @Autowired
    private AuditService auditService;

    @RequestMapping(value = "/heatData", method = RequestMethod.GET)
    public @ResponseBody List<HeatData> getHeatData()
    {
        List<HeatData> result = new ArrayList<HeatData>();

        List<UserDto> users = userService.getAllUsers(false);

        Calendar cal = Calendar.getInstance();
        int userPosition = 0;
        for (UserDto user : users)
        {
            Map<Integer, Integer> hours = initLoginsPerHour();

            for (Date date : auditService.getLoginAttemptsTime(user))
            {
                cal.setTime(date);
                Integer hour = cal.get(Calendar.HOUR_OF_DAY);
                hours.put(hour, hours.get(hour) + 1);
            }
            
            List<HeatDataPoint> dataPoints = new ArrayList<HeatDataPoint>();

            for (Integer hour : hours.keySet())
            {
                dataPoints.add(new HeatDataPoint(hour.toString() + ":00", hour, hours.get(hour)));
            }
            
            result.add(new HeatData(user.getLogin(), userPosition, dataPoints));
            userPosition++;
        }

        return result;
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

    /**
     * Behandelt GET-Requests vom Typ "/admin/statistic/global". Lädt alle Statistiken.
     * 
     * @return Template
     */
    @PreAuthorize("hasRole('" + AdministrationFeature.KEY + "_" + SecurityAdminAccessRole.KEY
            + "') OR hasRole('" + AdministrationFeature.KEY + "_" + SetupSystemAccessRole.KEY + "')")
    @RequestMapping(method = RequestMethod.GET)
    public String showGlobalStatistics(Model model)
    {
        return getLoadingRedirectTemplate();
    }

    protected String getLoadingRedirectTemplate()
    {
        return "feature/administration/loginAttemptsGlobal";
    }

    private Map<Integer, Integer> initLoginsPerHour()
    {
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();

        for (int hour = 0; hour < 24; ++hour)
        {
            result.put(hour, 0);
        }

        return result;
    }

    public class HeatData
    {
        private String              name;
        private int                 position;
        private List<HeatDataPoint> hours;

        public HeatData(String name, int position, List<HeatDataPoint> hours)
        {
            this.name = name;
            this.position = position;
            this.hours = hours;
        }

        public List<HeatDataPoint> getHours()
        {
            return hours;
        }

        public String getName()
        {
            return name;
        }

        public int getPosition()
        {
            return position;
        }

    }

    public class HeatDataPoint
    {
        private String name;
        private int    position;
        private int    count;

        public HeatDataPoint(String name, int position, int count)
        {
            this.name = name;
            this.position = position;
            this.count = count;
        }

        public int getCount()
        {
            return count;
        }

        public String getName()
        {
            return name;
        }

        public int getPosition()
        {
            return position;
        }
    }

    public class PieDataPoint
    {
        private String name;
        private int    countOk;
        private int    countFailed;

        public PieDataPoint(String name, int countOk, int countFailed)
        {
            this.name = name;
            this.countOk = countOk;
            this.countFailed = countFailed;
        }

        public int getCountFailed()
        {
            return countFailed;
        }

        public int getCountOk()
        {
            return countOk;
        }

        public String getName()
        {
            return name;
        }
    }
}
