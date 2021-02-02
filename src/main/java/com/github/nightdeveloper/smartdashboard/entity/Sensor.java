package com.github.nightdeveloper.smartdashboard.entity;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Date;

public class Sensor {

    @Id
    protected String id;
    protected String deviceId;
    protected LocalDateTime date;
    protected Long linkquality;

    public Sensor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getLinkquality() {
        return linkquality;
    }

    public void setLinkquality(Long linkquality) {
        this.linkquality = linkquality;
    }
}
