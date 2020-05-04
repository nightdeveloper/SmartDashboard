package com.github.nightdeveloper.smartdashboard.entity;

public class SwitchSensor extends BatterySensor {

    private String click;

    public SwitchSensor() {
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    @Override
    public String toString() {
        return "SwitchSensor{" +
                "voltage=" + voltage +
                ", click='" + click + '\'' +
                ", battery=" + battery +
                ", id='" + id + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", date=" + date +
                ", linkquality=" + linkquality +
                '}';
    }
}
