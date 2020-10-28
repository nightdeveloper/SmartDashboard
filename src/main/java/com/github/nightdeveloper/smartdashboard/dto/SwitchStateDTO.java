package com.github.nightdeveloper.smartdashboard.dto;

import java.time.LocalDateTime;

public class SwitchStateDTO {
    private String deviceId;
    private String state;
    private LocalDateTime date;

    public SwitchStateDTO() {
    }

    public SwitchStateDTO(String deviceId, String state, LocalDateTime date) {
        this.deviceId = deviceId;
        this.state = state;
        this.date = date;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "SwitchStateDTO{" +
                "deviceId='" + deviceId + '\'' +
                ", state='" + state + '\'' +
                ", date=" + date +
                '}';
    }
}
