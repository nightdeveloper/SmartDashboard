package com.github.nightdeveloper.smartdashboard;

import com.github.nightdeveloper.smartdashboard.service.SunService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.shredzone.commons.suncalc.SunTimes;

import java.time.ZonedDateTime;

public class SunsetSunriseTest {

    private static final Logger logger = LogManager.getLogger(SunsetSunriseTest.class);

    @Test
    public void testLib() {

        ZonedDateTime dateTime = ZonedDateTime.now();

        SunTimes times = SunTimes.compute()
            .on(dateTime)
            .at(SunService.moscowLat, SunService.moscowLng)
            .execute();

        logger.info("Sunrise: " + times.getRise());
        logger.info("Noon: " + times.getNoon());
        logger.info("Sunset: " + times.getSet());
        logger.info("Nadir: " + times.getNadir());
    }
}
