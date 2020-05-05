package com.github.nightdeveloper.smartdashboard.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix="devices")
public class DevicesProperty {

    private String location;
    private List<DeviceProperty> list;

    public DevicesProperty() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<DeviceProperty> getList() {
        return list;
    }

    public void setList(List<DeviceProperty> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "DevicesProperty{" +
                "location='" + location + '\'' +
                ", list=" + list +
                '}';
    }
}
