package com.github.nightdeveloper.smartdashboard.moex.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nightdeveloper.smartdashboard.moex.entity.AbstractMoexJsonEntity;
import com.github.nightdeveloper.smartdashboard.moex.model.IndexDTO;
import com.github.nightdeveloper.smartdashboard.moex.model.JsonCsvDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

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

    public void mapMoexDtoToEntity(JsonCsvDTO jsonCsvDTO, AbstractMoexJsonEntity entity)
            throws NoSuchFieldException, IllegalAccessException {

        for(List<String> dataRow : jsonCsvDTO.getData()) {
            int index = 0;
            for(String column : jsonCsvDTO.getColumns()) {
                try {
                    Field field = entity.getClass().getDeclaredField(column);
                    field.setAccessible(true);

                    String value = dataRow.get(index);
                    if (field.getType() == Long.class) {
                        if (value == null) {
                            field.set(entity, null);
                        } else {
                            field.set(entity, Long.parseLong(value));
                        }
                    } else {
                        field.set(entity, value);
                    }
                } catch (Throwable e) {
                    log.error("error mapping field {} for class {}", column, entity.getClass().getSimpleName());
                    throw e;
                }

                index++;
            }
        }
    }
}
