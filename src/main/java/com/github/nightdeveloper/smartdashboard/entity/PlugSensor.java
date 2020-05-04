package com.github.nightdeveloper.smartdashboard.entity;

// [{"state":"OFF","power":0,"voltage":null,"consumption":0.05,"temperature":29,"linkquality":76}]

import java.math.BigDecimal;

public class PlugSensor extends Sensor {

    private String state;
    private Long power;
    private Long voltage;
    private BigDecimal consumption;
    private BigDecimal temperature;

    public PlugSensor() {
    }

    public PlugSensor(String id, String state, Long power, Long voltage, BigDecimal consumption,
                      BigDecimal temperature, Long linkquality) {
        this.id = id;
        this.state = state;
        this.power = power;
        this.voltage = voltage;
        this.consumption = consumption;
        this.temperature = temperature;
        this.linkquality = linkquality;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getPower() {
        return power;
    }

    public void setPower(Long power) {
        this.power = power;
    }

    public Long getVoltage() {
        return voltage;
    }

    public void setVoltage(Long voltage) {
        this.voltage = voltage;
    }

    public BigDecimal getConsumption() {
        return consumption;
    }

    public void setConsumption(BigDecimal consumption) {
        this.consumption = consumption;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public Long getLinkquality() {
        return linkquality;
    }

    public void setLinkquality(Long linkquality) {
        this.linkquality = linkquality;
    }

    @Override
    public String toString() {
        return "PlugSensor{" +
                "id='" + id + '\'' +
                ", state='" + state + '\'' +
                ", power=" + power +
                ", voltage=" + voltage +
                ", consumption=" + consumption +
                ", temperature=" + temperature +
                ", linkquality=" + linkquality +
                '}';
    }
}
