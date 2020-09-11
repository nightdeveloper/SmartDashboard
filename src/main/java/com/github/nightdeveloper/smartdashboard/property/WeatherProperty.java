package com.github.nightdeveloper.smartdashboard.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="weather")
public class WeatherProperty {

    private String gismeteoHash;
    private Long forecaId;
    private Long yandexCid1;
    private Long yandexCid2;

    public WeatherProperty() {
    }

    public String getGismeteoHash() {
        return gismeteoHash;
    }

    public void setGismeteoHash(String gismeteoHash) {
        this.gismeteoHash = gismeteoHash;
    }

    public Long getForecaId() {
        return forecaId;
    }

    public void setForecaId(Long forecaId) {
        this.forecaId = forecaId;
    }

    public Long getYandexCid1() {
        return yandexCid1;
    }

    public void setYandexCid1(Long yandexCid1) {
        this.yandexCid1 = yandexCid1;
    }

    public Long getYandexCid2() {
        return yandexCid2;
    }

    public void setYandexCid2(Long yandexCid2) {
        this.yandexCid2 = yandexCid2;
    }

    @Override
    public String toString() {
        return "WeatherProperty{" +
                "gismeteoHash='" + gismeteoHash + '\'' +
                ", forecaId=" + forecaId +
                ", yandexCid1=" + yandexCid1 +
                ", yandexCid2=" + yandexCid2 +
                '}';
    }
}
