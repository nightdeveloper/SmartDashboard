package com.github.nightdeveloper.smartdashboard.controller;

import com.github.nightdeveloper.smartdashboard.common.Constants;
import com.github.nightdeveloper.smartdashboard.property.CameraProperty;
import com.github.nightdeveloper.smartdashboard.property.CamerasProperty;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class ImagesController {

    final static Logger logger = LogManager.getLogger(ImagesController.class);

    @Autowired
    private CamerasProperty camerasProperty;

    @RequestMapping(value = Constants.ENDPOINT_IMAGES, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView images(Principal principal) {

        logger.info("images root " + principal.getName());

        ModelAndView modelAndView = new ModelAndView("images");
        Map<String, Object> model = modelAndView.getModel();
        model.put("cameras", camerasProperty.getList());

        return modelAndView;
    }

    private byte[] getNAImage() {
        try {
            File file = ResourceUtils.getFile("classpath:na.jpg");
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            logger.error("can't read n/a image", e);
        }
        return null;
    }

    @RequestMapping(value = Constants.ENDPOINT_IMAGE, method = RequestMethod.GET)
    @ResponseBody
    public byte[] image(Principal principal,
                        @RequestParam int index) {

        logger.info("images root " + principal.getName());

        List<CameraProperty> cameras = camerasProperty.getList();

        if (index < 0 || index > cameras.size()) {
            return getNAImage();
        }

        try {
            CameraProperty cameraProperty = cameras.get(index);
            String url = cameraProperty.getUrl() + cameraProperty.getCurrent();

            logger.info("requesting image from " + url);

            try (InputStream inputStream = new URL(url).openStream()) {
                logger.info("returning image");
                return IOUtils.toByteArray(inputStream);
            }
        } catch(Throwable e) {
            logger.error("getting image from camera error", e);
            return getNAImage();
        }
    }
}
