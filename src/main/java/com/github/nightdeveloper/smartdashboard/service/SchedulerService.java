package com.github.nightdeveloper.smartdashboard.service;

import com.github.nightdeveloper.smartdashboard.constants.Profiles;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Profile("!" + Profiles.TEST)
public class SchedulerService {

    private static final Logger logger = LogManager.getLogger(SchedulerService.class);

    private final ValuteService valuteService;

    private final ArchiveService archiveService;

    public SchedulerService(ValuteService valuteService, ArchiveService archiveService) {
        this.valuteService = valuteService;
        this.archiveService = archiveService;
    }

    @Scheduled(cron = "${cameras.archive.cron}")
    public void scheduleArchive() {
        logger.info("scheduleArchive");
        archiveService.getCamerasAndMoveToArchive();
    }

    @Scheduled(fixedDelay = 12 * 60 * 60 * 1000 )
    public void scheduleValuteRateUpdate() {
        logger.info("scheduleValuteRateUpdate");
        valuteService.saveFavouriteValuteRate(valuteService.requestCurrentValute());
    }
}
