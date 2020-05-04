package com.github.nightdeveloper.smartdashboard.entity;

import java.math.BigDecimal;

/*
[{"battery":51,"voltage":2915,"temperature":13.09,"humidity":45.24,"pressure":989,"linkquality":63}]
 */

public class ComfortSensor extends BatterySensor {

    private BigDecimal temperature;
    private BigDecimal humidity;
    private Long pressure;

    public ComfortSensor() {
    }

    public Long getBattery() {
        return battery;
    }

    public void setBattery(Long battery) {
        this.battery = battery;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    public Long getPressure() {
        return pressure;
    }

    public void setPressure(Long pressure) {
        this.pressure = pressure;
    }

    public Long getLinkquality() {
        return linkquality;
    }

    public void setLinkquality(Long linkquality) {
        this.linkquality = linkquality;
    }

    @Override
    public String toString() {
        return "ComfortSensor{" +
                "id='" + id + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", date=" + date +
                ", battery=" + battery +
                ", voltage=" + voltage +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", linkquality=" + linkquality +
                '}';
    }
}
