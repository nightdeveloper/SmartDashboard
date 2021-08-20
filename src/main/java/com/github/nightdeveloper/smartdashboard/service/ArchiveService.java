package com.github.nightdeveloper.smartdashboard.service;

import com.github.nightdeveloper.smartdashboard.property.CameraProperty;
import com.github.nightdeveloper.smartdashboard.property.CamerasProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class ArchiveService {

    private static final DateTimeFormatter valuteDateFormatter =
            DateTimeFormatter.ofPattern("yyyy_MM_dd__hh_mm");

    private final CamerasProperty camerasProperty;

    private final UrlService urlService;

    private final FileService fileService;

    public ArchiveService(CamerasProperty camerasProperty, UrlService urlService,
                          FileService fileService) {
        this.camerasProperty = camerasProperty;
        this.urlService = urlService;
        this.fileService = fileService;
    }

    public void getCamerasAndMoveToArchive() {

        List<CameraProperty> cameras = camerasProperty.getList();

        String currentTimeStamp = valuteDateFormatter.format(LocalDateTime.now());

        for(CameraProperty cameraProperty : cameras) {

            String path = camerasProperty.getArchive().getPath() + "/camera_" +
                    cameraProperty.getName().replace(" ", "_") +
                    "_" + currentTimeStamp + ".jpg";

            String url = cameraProperty.getUrl() + cameraProperty.getCurrent();

            log.info("get image from url " + url + " to path " + path);

            try (OutputStream outputStream = fileService.getFileOutputStream(path)){
                try {
                    urlService.copyCameraImageToStream(url, outputStream);
                } catch (Throwable e) {
                    log.error("can't get image", e);
                }
            } catch (Throwable e) {
                log.error("can't put image", e);
            }
        }

    }
}
