package com.github.nightdeveloper.smartdashboard.entity;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class Sensor {

    @Id
    protected String id;
    protected String deviceId;
    protected Date date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getLinkquality() {
        return linkquality;
    }

    public void setLinkquality(Long linkquality) {
        this.linkquality = linkquality;
    }
}
