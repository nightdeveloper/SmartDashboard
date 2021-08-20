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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class DashboardController {

    private final SensorService sensorService;
    private final SunService sunService;

    private final Devices devices;

    private final CamerasProperty camerasProperty;
    private final WeatherProperty weatherProperty;

    private final ValuteAggregationRepository valuteAggregationRepository;

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

    @GetMapping(value = Constants.ENDPOINT_DASHBOARD)
    @ResponseBody
    public ModelAndView dashboard(Principal principal) {
        log.info("opened dashboard " + principal.getName());

        long timeStart = System.currentTimeMillis();

        ModelAndView modelAndView = new ModelAndView("dashboard");

        Map<String, Object> model = modelAndView.getModel();

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
        if (token.getPrincipal() != null) {
            model.put("userInfo", token.getPrincipal().getAttributes());
        }

        // smart home
        model.put("devices", devices.getDevices());

        log.info("getting temperatures");
        model.put("temperatures", sensorService.getLastTemperatures());

        log.info("getting humidity");
        model.put("humidity", sensorService.getLastHumidity());

        log.info("getting pressure");
        model.put("pressure", sensorService.getLastPressure());

        log.info("getting linkQuality");
        model.put("linkQuality", sensorService.getLastLinkQuality());

        log.info("getting lastBattery");
        List<AverageDeviceValueDTO> lastBattery =
                sensorService.getLastBattery();

        log.info("getting VoltageListToPercentageList");
        Utils.batteryVoltageListToPercentageList(lastBattery);

        log.info("getting battery");
        model.put("battery", lastBattery);

        log.info("getting batteryStatus");
        model.put("batteryStatus", sensorService.getBatteryStatus(lastBattery));

        log.info("getting switchStatus");
        model.put("switchStatus", sensorService.getPlugsState());

        // rate list
        log.info("getting rates");
        model.put("rates", valuteAggregationRepository.getValuteByPeriod(
                new ArrayList<ValuteConst>() {{
                    add(ValuteConst.USD);
                    add(ValuteConst.EUR);
                }}, 45));

        // weather
        model.put("weatherProperty", weatherProperty);

        log.info("getting sunInfo");
        model.put("sunInfo", sunService.getSunInfo());

        // cameras
        model.put("cameras", camerasProperty.getList());

        log.info("dashboard generated for  " + (System.currentTimeMillis() - timeStart) + " nano");

        return modelAndView;
    }

}
