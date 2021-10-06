package com.github.nightdeveloper.smartdashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nightdeveloper.smartdashboard.constants.Profiles;
import com.github.nightdeveloper.smartdashboard.moex.common.MoexHttpFacade;
import com.github.nightdeveloper.smartdashboard.moex.model.IndexDTO;
import com.github.nightdeveloper.smartdashboard.moex.model.JsonCsvDTO;
import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = Profiles.TEST)
class MoexTest {

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected MoexHttpFacade moexHttpFacade;

    private void assertJsonCsvDto(JsonCsvDTO jsonCsvDTO) {
        Assertions.assertNotNull(jsonCsvDTO);

        Assertions.assertNotNull(jsonCsvDTO.getMetadata());
        Assertions.assertTrue(jsonCsvDTO.getMetadata().size() > 0);

        Assertions.assertNotNull(jsonCsvDTO.getColumns());
        Assertions.assertTrue(jsonCsvDTO.getColumns().size() > 0);

        Assertions.assertNotNull(jsonCsvDTO.getData());
        Assertions.assertTrue(jsonCsvDTO.getData().size() > 0);
    }

    @Test
    void testIndexParsing(IndexDTO indexDTO) {
        Assertions.assertNotNull(indexDTO);
        assertJsonCsvDto(indexDTO.getEngines());
        assertJsonCsvDto(indexDTO.getMarkets());
        assertJsonCsvDto(indexDTO.getBoards());
        assertJsonCsvDto(indexDTO.getBoardgroups());
        assertJsonCsvDto(indexDTO.getDurations());
        assertJsonCsvDto(indexDTO.getSecuritytypes());
        assertJsonCsvDto(indexDTO.getSecuritygroups());
        assertJsonCsvDto(indexDTO.getSecuritycollections());
    }

    @Test
    void testIndexLoading() throws IOException {
        IndexDTO indexDTO = moexHttpFacade.getIndex();
        testIndexParsing(indexDTO);
    }

    @Test
    void testIndexPreloaded() throws IOException {
        File jsonFile = new File("src/test/resources/moex/index.json");
        Assertions.assertTrue(jsonFile.exists());

        IndexDTO indexDTO = mapper.readValue(jsonFile, IndexDTO.class);
        testIndexParsing(indexDTO);
    }
}
