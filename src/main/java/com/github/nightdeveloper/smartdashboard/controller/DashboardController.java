package com.github.nightdeveloper.smartdashboard.controller;

import com.github.nightdeveloper.smartdashboard.common.Constants;
import com.github.nightdeveloper.smartdashboard.common.Devices;
import com.github.nightdeveloper.smartdashboard.common.Utils;
import com.github.nightdeveloper.smartdashboard.constants.ValuteConst;
import com.github.nightdeveloper.smartdashboard.dto.AverageDeviceValueDTO;
import com.github.nightdeveloper.smartdashboard.property.CamerasProperty;
import com.github.nightdeveloper.smartdashboard.property.WeatherProperty;
import com.github.nightdeveloper.smartdashboard.repository.ValuteAggregationRepository;
import com.github.nightdeveloper.smartdashboard.service.SensorService;
import com.github.nightdeveloper.smartdashboard.service.SunService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private static final Logger logger = LogManager.getLogger(IndexController.class);

    final private SensorService sensorService;
    final private SunService sunService;

    final private Devices devices;

    final private CamerasProperty camerasProperty;
    final private WeatherProperty weatherProperty;

    final private ValuteAggregationRepository valuteAggregationRepository;

    public DashboardController(SensorService sensorService, Devices devices, CamerasProperty camerasProperty,
                               ValuteAggregationRepository valuteAggregationRepository,
                               WeatherProperty weatherProperty, SunService sunService) {
        this.sensorService = sensorService;
        this.devices = devices;
        this.camerasProperty = camerasProperty;
        this.valuteAggregationRepository = valuteAggregationRepository;
        this.weatherProperty = weatherProperty;
        this.sunService = sunService;
    }

    @RequestMapping(value = Constants.ENDPOINT_DASHBOARD, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView dashboard(Principal principal) {

        logger.info("opened dashboard " + principal.getName());

        long timeStart = System.currentTimeMillis();

        ModelAndView modelAndView = new ModelAndView("dashboard");

        Map<String, Object> model = modelAndView.getModel();

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
        if (token.getPrincipal() != null) {
            model.put("userInfo", token.getPrincipal().getAttributes());
        }

        // smart home
        model.put("devices", devices.getDevices());

        logger.info("getting temperatures");
        model.put("temperatures", sensorService.getLastTemperatures());

        logger.info("getting humidity");
        model.put("humidity", sensorService.getLastHumidity());

        logger.info("getting pressure");
        model.put("pressure", sensorService.getLastPressure());

        logger.info("getting linkQuality");
        model.put("linkQuality", sensorService.getLastLinkQuality());

        logger.info("getting lastBattery");
        List<AverageDeviceValueDTO> lastBattery =
                sensorService.getLastBattery();

        logger.info("getting VoltageListToPercentageList");
        Utils.batteryVoltageListToPercentageList(lastBattery);

        logger.info("getting battery");
        model.put("battery", lastBattery);

        logger.info("getting batteryStatus");
        model.put("batteryStatus", sensorService.getBatteryStatus(lastBattery));

        logger.info("getting switchStatus");
        model.put("switchStatus", sensorService.getPlugsState());

        // rate list
        logger.info("getting rates");
        model.put("rates", valuteAggregationRepository.getValuteByPeriod(
                new ArrayList<ValuteConst>() {{
                    add(ValuteConst.USD);
                    add(ValuteConst.EUR);
                }}, 45));

        // weather
        model.put("weatherProperty", weatherProperty);

        logger.info("getting sunInfo");
        model.put("sunInfo", sunService.getSunInfo());

        // cameras
        model.put("cameras", camerasProperty.getList());

        logger.info("dashboard generated for  " + (System.currentTimeMillis() - timeStart) + " nano");

        return modelAndView;
    }

}
