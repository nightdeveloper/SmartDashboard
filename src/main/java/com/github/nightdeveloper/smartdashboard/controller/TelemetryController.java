package com.github.nightdeveloper.smartdashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nightdeveloper.smartdashboard.common.Device;
import com.github.nightdeveloper.smartdashboard.common.Devices;
import com.github.nightdeveloper.smartdashboard.entity.Sensor;
import com.github.nightdeveloper.smartdashboard.repository.SensorRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

import static com.github.nightdeveloper.smartdashboard.common.Constants.*;

@Controller
public class TelemetryController {

    private static final Logger logger = LogManager.getLogger(TelemetryController.class);

    private final ObjectMapper objectMapper;
    private final SensorRepository repository;
    private final Devices devices;

    public TelemetryController(ObjectMapper objectMapper, SensorRepository repository, Devices devices) {
        this.objectMapper = objectMapper;
        this.repository = repository;
        this.devices = devices;
    }

    @RequestMapping(value = REST_TELEMETRY, method = RequestMethod.POST, produces = JSON_UTF8)
    @ResponseBody
    public String login(@RequestBody String request) {

        logger.info("got request " + request);

        if (request == null || request.length() == 0) {
            logger.error("empty request");
            return RESPONSE_FAILED;
        }

        try {
            Sensor baseData = objectMapper.readValue(request, Sensor.class);

            Device<?> device = devices.findDeviceById(baseData.getDeviceId());

            if (device == null) {
                logger.info("no device - not setting");
                return "device not found with id = " + baseData.getDeviceId();
            }

            Sensor fullData = (Sensor)objectMapper.readValue(request, device.getWrappingClass());
            fullData.setDate(new Date());

            logger.info("got info from " + device.getName() + " " + fullData);

            repository.save(fullData);

            return RESPONSE_OK;

        } catch (Throwable e) {
            logger.error("error while parsing request", e);

            return RESPONSE_FAILED;
        }
    }
}
