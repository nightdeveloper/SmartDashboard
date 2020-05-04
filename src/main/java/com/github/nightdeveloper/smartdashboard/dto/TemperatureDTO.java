package com.github.nightdeveloper.smartdashboard.dto;

import java.math.BigDecimal;
import java.util.Date;

public class TemperatureDTO {

    private String deviceId;
    private BigDecimal temperature;
    private Date date;

    public TemperatureDTO() {
    }

    public TemperatureDTO(String deviceId, BigDecimal temperature, Date date) {
        this.deviceId = deviceId;
        this.temperature = temperature;
        this.date = date;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TemperatureDTO{" +
                "deviceId='" + deviceId + '\'' +
                ", temperature='" + temperature + '\'' +
                ", date=" + date +
                '}';
    }
}
