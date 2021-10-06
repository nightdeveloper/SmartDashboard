package com.github.nightdeveloper.smartdashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nightdeveloper.smartdashboard.constants.Profiles;
import com.github.nightdeveloper.smartdashboard.moex.common.MoexHttpFacade;
import com.github.nightdeveloper.smartdashboard.moex.entity.*;
import com.github.nightdeveloper.smartdashboard.moex.model.IndexDTO;
import com.github.nightdeveloper.smartdashboard.moex.model.JsonCsvDTO;
import com.github.nightdeveloper.smartdashboard.moex.repository.EngineRepository;
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

    @Autowired
    protected EngineRepository engineRepository;

    private void assertJsonCsvDto(JsonCsvDTO jsonCsvDTO, AbstractMoexJsonEntity entity) throws Exception {
        Assertions.assertNotNull(jsonCsvDTO);

        Assertions.assertNotNull(jsonCsvDTO.getMetadata());
        Assertions.assertTrue(jsonCsvDTO.getMetadata().size() > 0);

        Assertions.assertNotNull(jsonCsvDTO.getColumns());
        Assertions.assertTrue(jsonCsvDTO.getColumns().size() > 0);

        Assertions.assertNotNull(jsonCsvDTO.getData());
        Assertions.assertTrue(jsonCsvDTO.getData().size() > 0);

        moexHttpFacade.mapMoexDtoToEntity(jsonCsvDTO, entity);
    }

    @Test
    void testIndexParsing(IndexDTO indexDTO) throws Exception {
        Assertions.assertNotNull(indexDTO);
        assertJsonCsvDto(indexDTO.getEngines(), new Engine());
        assertJsonCsvDto(indexDTO.getMarkets(), new Market());
        assertJsonCsvDto(indexDTO.getBoards(), new Board());
        assertJsonCsvDto(indexDTO.getBoardgroups(), new BoardGroup());
        assertJsonCsvDto(indexDTO.getDurations(), new Duration());
        assertJsonCsvDto(indexDTO.getSecuritytypes(), new SecurityType());
        assertJsonCsvDto(indexDTO.getSecuritygroups(), new SecurityGroup());
        assertJsonCsvDto(indexDTO.getSecuritycollections(), new SecurityCollection());
    }

    @Test
    void testIndexLoading() throws Exception {
        IndexDTO indexDTO = moexHttpFacade.getIndex();
        testIndexParsing(indexDTO);
    }

    @Test
    void testIndexPreloaded() throws Exception {
        File jsonFile = new File("src/test/resources/moex/index.json");
        Assertions.assertTrue(jsonFile.exists());

        IndexDTO indexDTO = mapper.readValue(jsonFile, IndexDTO.class);
        testIndexParsing(indexDTO);
    }

    @Test
    void testRepository() {
        Engine testEngine = new Engine();
        testEngine.setName("test");
        testEngine.setTitle("title");

        testEngine = engineRepository.save(testEngine);

        Assertions.assertNotNull(testEngine.getId());

        engineRepository.delete(testEngine);
    }
}
