package com.github.nightdeveloper.smartdashboard.common;

import com.github.nightdeveloper.smartdashboard.entity.ComfortSensor;
import com.github.nightdeveloper.smartdashboard.entity.PlugSensor;
import com.github.nightdeveloper.smartdashboard.entity.SwitchSensor;
import com.github.nightdeveloper.smartdashboard.property.DeviceProperty;
import com.github.nightdeveloper.smartdashboard.property.DevicesProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Devices {

    private final List<Device<?>> devices;

    public Devices(DevicesProperty devicesList) {
        devices = new ArrayList<>();

        for (DeviceProperty deviceProperty : devicesList.getList()) {
            String id = deviceProperty.getId();
            String name = deviceProperty.getName();
            String location = deviceProperty.getLocation();
            String className = deviceProperty.getClassName();
            switch(className) {
                case "SwitchSensor":
                    devices.add(new Device<SwitchSensor>(id, name, location, SwitchSensor.class));
                    break;
                case "ComfortSensor":
                    devices.add(new Device<ComfortSensor>(id, name, location, ComfortSensor.class));
                    break;
                case "PlugSensor":
                    devices.add(new Device<PlugSensor>(id, name, location, PlugSensor.class));
                    break;
                default:
                    throw new RuntimeException("Invalid sensor type - " + className);
            }
        }
    }

    public List<Device<?>> getDevices() {
        return Collections.unmodifiableList(devices);
    }

    public Device<?> findDeviceById(String id) {
        List<Device<?>> filtered =
                devices.stream().filter(device -> device.getId().equals(id)).collect(Collectors.toList());

        if (filtered.size() == 1) {
            return filtered.get(0);
        }

        return null;
    }
}
