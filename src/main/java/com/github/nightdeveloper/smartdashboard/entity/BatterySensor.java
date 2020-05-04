package com.github.nightdeveloper.smartdashboard.entity;

public abstract class BatterySensor extends Sensor {

    protected Long voltage;
    protected Long battery;

    public BatterySensor() {
    }

    public Long getBattery() {
        return battery;
    }

    public void setBattery(Long battery) {
        this.battery = battery;
    }

    public Long getVoltage() {
        return voltage;
    }

    public void setVoltage(Long voltage) {
        this.voltage = voltage;
    }

    @Override
    public String toString() {
        return "BatterySensor{" +
                "voltage=" + voltage +
                ", battery=" + battery +
                ", id='" + id + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", date=" + date +
                ", linkquality=" + linkquality +
                '}';
    }
}
