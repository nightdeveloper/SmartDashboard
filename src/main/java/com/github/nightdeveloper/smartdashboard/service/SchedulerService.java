package com.github.nightdeveloper.smartdashboard.service;

import com.github.nightdeveloper.smartdashboard.constants.Profiles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Profile("!" + Profiles.TEST)
@Slf4j
public class SchedulerService {
    private final ValuteService valuteService;

    private final ArchiveService archiveService;

    public SchedulerService(ValuteService valuteService, ArchiveService archiveService) {
        this.valuteService = valuteService;
        this.archiveService = archiveService;
    }

    @Scheduled(cron = "${cameras.archive.cron}")
    public void scheduleArchive() {
        log.info("scheduleArchive");
        archiveService.getCamerasAndMoveToArchive();
    }

    @Scheduled(fixedDelay = 12 * 60 * 60 * 1000 )
    public void scheduleValuteRateUpdate() {
        log.info("scheduleValuteRateUpdate");
        valuteService.saveFavouriteValuteRate(valuteService.requestCurrentValute());
    }
}
