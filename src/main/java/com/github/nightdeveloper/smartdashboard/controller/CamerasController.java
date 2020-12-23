package com.github.nightdeveloper.smartdashboard.controller;

import com.github.nightdeveloper.smartdashboard.common.Constants;
import com.github.nightdeveloper.smartdashboard.property.CameraProperty;
import com.github.nightdeveloper.smartdashboard.property.CamerasProperty;
import com.github.nightdeveloper.smartdashboard.service.UrlService;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Principal;
import java.util.List;

@Controller
public class CamerasController {

    final static Logger logger = LogManager.getLogger(CamerasController.class);

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
            logger.error("can't read n/a image", e);
        }
    }

    @RequestMapping(value = Constants.ENDPOINT_IMAGE, method = RequestMethod.GET)
    @ResponseBody
    public void image(Principal principal,
                        HttpServletResponse response,
                        @RequestParam int index) {

        logger.info("images root " + principal.getName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        List<CameraProperty> cameras = camerasProperty.getList();

        if (index < 0 || index > cameras.size()) {
            logger.error("incorrect camera index");
            returnNAImage(response);
            return;
        }

        try {
            CameraProperty cameraProperty = cameras.get(index);
            String url = cameraProperty.getUrl() + cameraProperty.getCurrent();

            logger.info("requesting image from " + url);
            urlService.copyCameraImageToStream(url, response.getOutputStream());

        } catch(Throwable e) {
            logger.error("getting image from camera error", e);
            returnNAImage(response);
        }
    }
}
