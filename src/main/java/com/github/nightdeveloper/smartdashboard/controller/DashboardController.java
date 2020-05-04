package com.github.nightdeveloper.smartdashboard.controller;

import com.github.nightdeveloper.smartdashboard.common.Constants;
import com.github.nightdeveloper.smartdashboard.common.Devices;
import com.github.nightdeveloper.smartdashboard.dto.AverageDeviceValueDTO;
import com.github.nightdeveloper.smartdashboard.service.SensorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private static final Logger logger = LogManager.getLogger(IndexController.class);

    final private SensorService sensorService;
    final private Devices devices;

    public DashboardController(SensorService sensorService, Devices devices) {
        this.sensorService = sensorService;
        this.devices = devices;
    }

    @RequestMapping(value = Constants.ENDPOINT_DASHBOARD, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView dashboard(Principal principal) {

        logger.info("opened dashboard " + principal.getName());

        ModelAndView modelAndView = new ModelAndView("dashboard");

        Map<String, Object> model = modelAndView.getModel();

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
        if (token.getPrincipal() != null) {
            model.put("userInfo", token.getPrincipal().getAttributes());
        }
        
        model.put("devices", devices.getDevices());

        model.put("temperatures", sensorService.getLastTemperatures());
        model.put("humidity", sensorService.getLastHumidity());
        model.put("pressure", sensorService.getLastPressure());
        model.put("linkQuality", sensorService.getLastLinkQuality());

        List<AverageDeviceValueDTO> lastBattery = sensorService.getLastBattery();
        model.put("battery", lastBattery);
        model.put("batteryStatus", sensorService.getBatteryStatus(lastBattery));

        return modelAndView;
    }

}
