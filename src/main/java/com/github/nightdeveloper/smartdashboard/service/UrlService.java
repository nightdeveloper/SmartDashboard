package com.github.nightdeveloper.smartdashboard.service;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

@Service
public class UrlService {

    final static Logger logger = LogManager.getLogger(UrlService.class);

    public void copyCameraImageToStream(String url, OutputStream outputStream) throws IOException {

        try (InputStream inputStream = new URL(url).openStream()) {
            logger.info("returning image");
            IOUtils.copy(inputStream, outputStream);
        }
    }
}
