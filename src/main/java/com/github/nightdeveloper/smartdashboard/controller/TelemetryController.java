package com.github.nightdeveloper.smartdashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nightdeveloper.smartdashboard.common.Device;
import com.github.nightdeveloper.smartdashboard.common.Devices;
import com.github.nightdeveloper.smartdashboard.entity.Sensor;
import com.github.nightdeveloper.smartdashboard.repository.SensorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.github.nightdeveloper.smartdashboard.common.Constants.*;

@Controller
@Slf4j
public class TelemetryController {

    private final ObjectMapper objectMapper;
    private final SensorRepository repository;
    private final Devices devices;

    public TelemetryController(ObjectMapper objectMapper, SensorRepository repository, Devices devices) {
        this.objectMapper = objectMapper;
        this.repository = repository;
        this.devices = devices;
    }

    @PostMapping(value = REST_TELEMETRY, produces = JSON_UTF8)
    @ResponseBody
    public String login(@RequestBody String request) {

        log.info("got request " + request);

        if (request == null || request.length() == 0) {
            log.error("empty request");
            return RESPONSE_FAILED;
        }

        try {
            Sensor baseData = objectMapper.readValue(request, Sensor.class);

            Device<?> device = devices.findDeviceById(baseData.getDeviceId());

            if (device == null) {
                log.info("no device - not setting");
                return "device not found with id = " + baseData.getDeviceId();
            }

            Sensor fullData = (Sensor)objectMapper.readValue(request, device.getWrappingClass());
            fullData.setDate(LocalDateTime.now());

            log.info("got info from " + device.getName() + " " + fullData);

            repository.save(fullData);

            return RESPONSE_OK;

        } catch (Throwable e) {
            log.error("error while parsing request", e);

            return RESPONSE_FAILED;
        }
    }
}
