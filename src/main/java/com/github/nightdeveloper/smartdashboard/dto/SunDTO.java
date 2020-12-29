package com.github.nightdeveloper.smartdashboard.dto;

import java.time.ZonedDateTime;

public class SunDTO {

    private ZonedDateTime sunRise;
    private ZonedDateTime sunNoon;
    private ZonedDateTime sunSet;

    public SunDTO() {
    }

    public SunDTO(ZonedDateTime sunRise, ZonedDateTime sunNoon, ZonedDateTime sunSet) {
        this.sunRise = sunRise;
        this.sunNoon = sunNoon;
        this.sunSet = sunSet;
    }

    public ZonedDateTime getSunRise() {
        return sunRise;
    }

    public void setSunRise(ZonedDateTime sunRise) {
        this.sunRise = sunRise;
    }

    public ZonedDateTime getSunNoon() {
        return sunNoon;
    }

    public void setSunNoon(ZonedDateTime sunNoon) {
        this.sunNoon = sunNoon;
    }

    public ZonedDateTime getSunSet() {
        return sunSet;
    }

    public void setSunSet(ZonedDateTime sunSet) {
        this.sunSet = sunSet;
    }

    @Override
    public String toString() {
        return "SunDTO{" +
                "sunRise=" + sunRise +
                ", sunNoon=" + sunNoon +
                ", sunSet=" + sunSet +
                '}';
    }
}
