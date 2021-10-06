package com.github.nightdeveloper.smartdashboard.moex.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nightdeveloper.smartdashboard.moex.model.IndexDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Service
public class MoexHttpFacade {
    public static final String URL_INDEX = "https://iss.moex.com/iss/index.json";

    ObjectMapper objectMapper;

    private CloseableHttpClient getHttpClient() {
        return HttpClients.createDefault();
    }

    public IndexDTO getIndex() throws IOException {
        log.info("getting index");

        try(CloseableHttpClient client = getHttpClient()) {

            HttpGet httpGet = new HttpGet(URL_INDEX);
            CloseableHttpResponse response = client.execute(httpGet);

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.error("get categories error {}", response.getStatusLine());
                throw new IOException("invalid response code");
            }

            IndexDTO index = objectMapper.readValue(response.getEntity().getContent(), IndexDTO.class);

            log.info("got index {}", index.toString().length());

            return index;
        }
    }
}
