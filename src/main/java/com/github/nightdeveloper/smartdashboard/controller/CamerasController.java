package com.github.nightdeveloper.smartdashboard.controller;

import com.github.nightdeveloper.smartdashboard.common.Constants;
import com.github.nightdeveloper.smartdashboard.property.CameraProperty;
import com.github.nightdeveloper.smartdashboard.property.CamerasProperty;
import com.github.nightdeveloper.smartdashboard.service.UrlService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
public class CamerasController {

    private final CamerasProperty camerasProperty;

    private final UrlService urlService;

    public CamerasController(CamerasProperty camerasProperty, UrlService urlService) {
        this.camerasProperty = camerasProperty;
        this.urlService = urlService;
    }

    private void returnNAImage(HttpServletResponse response) {
        try {
            InputStream in = CamerasController.class.getResourceAsStream("/na.jpg");
            IOUtils.copy(in, response.getOutputStream());
        } catch (IOException e) {
            log.error("can't read n/a image", e);
        }
    }

    @GetMapping(value = Constants.ENDPOINT_IMAGE)
    @ResponseBody
    public void image(Principal principal,
                        HttpServletResponse response,
                        @RequestParam int index) {

        log.info("images root " + principal.getName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        List<CameraProperty> cameras = camerasProperty.getList();

        if (index < 0 || index > cameras.size()) {
            log.error("incorrect camera index");
            returnNAImage(response);
            return;
        }

        try {
            CameraProperty cameraProperty = cameras.get(index);
            String url = cameraProperty.getUrl() + cameraProperty.getCurrent();

            log.info("requesting image from " + url);
            urlService.copyCameraImageToStream(url, response.getOutputStream());

        } catch(Throwable e) {
            log.error("getting image from camera error", e);
            returnNAImage(response);
        }
    }
}
