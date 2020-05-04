package com.github.nightdeveloper.smartdashboard.dto;

import java.time.LocalDateTime;

public class BatteryStatusDTO {

    private String deviceId;

    private Double currentValue;
    private LocalDateTime currentValueDate;

    private Double lastMaxValue;
    private LocalDateTime lastMaxValueDate;

    private LocalDateTime dateDischarge;
    private long daysLeft;

    public BatteryStatusDTO() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
    }

    public LocalDateTime getCurrentValueDate() {
        return currentValueDate;
    }

    public void setCurrentValueDate(LocalDateTime currentValueDate) {
        this.currentValueDate = currentValueDate;
    }

    public Double getLastMaxValue() {
        return lastMaxValue;
    }

    public void setLastMaxValue(Double lastMaxValue) {
        this.lastMaxValue = lastMaxValue;
    }

    public LocalDateTime getLastMaxValueDate() {
        return lastMaxValueDate;
    }

    public void setLastMaxValueDate(LocalDateTime lastMaxValueDate) {
        this.lastMaxValueDate = lastMaxValueDate;
    }

    public LocalDateTime getDateDischarge() {
        return dateDischarge;
    }

    public void setDateDischarge(LocalDateTime dateDischarge) {
        this.dateDischarge = dateDischarge;
    }

    public long getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(long daysLeft) {
        this.daysLeft = daysLeft;
    }

    @Override
    public String toString() {
        return "BatteryStatusDTO{" +
                "deviceId='" + deviceId + '\'' +
                ", currentValue=" + currentValue +
                ", currentValueDate=" + currentValueDate +
                ", lastMaxValue=" + lastMaxValue +
                ", lastMaxValueDate=" + lastMaxValueDate +
                ", dateDischarge=" + dateDischarge +
                ", daysLeft=" + daysLeft +
                '}';
    }
}
