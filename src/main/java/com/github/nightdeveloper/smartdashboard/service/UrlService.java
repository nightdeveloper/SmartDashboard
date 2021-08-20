package com.github.nightdeveloper.smartdashboard.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

@Service
@Slf4j
public class UrlService {

    public void copyCameraImageToStream(String url, OutputStream outputStream) throws IOException {

        try (InputStream inputStream = new URL(url).openStream()) {
            log.info("returning image");
            IOUtils.copy(inputStream, outputStream);
        }
    }
}
