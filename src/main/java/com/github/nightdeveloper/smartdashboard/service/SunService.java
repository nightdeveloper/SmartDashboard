package com.github.nightdeveloper.smartdashboard.service;

import com.github.nightdeveloper.smartdashboard.dto.SunDTO;
import org.shredzone.commons.suncalc.SunTimes;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class SunService {

    public static final double moscowLat = 55.819011;
    public static final double moscowLng = 37.548977;

    public SunDTO getSunInfo() {
        SunTimes times = SunTimes.compute()
                .on(ZonedDateTime.now())
                .at(SunService.moscowLat, SunService.moscowLng)
                .execute();

        return new SunDTO(times.getRise(), times.getNoon(), times.getSet());
    }
}
