package com.github.nightdeveloper.smartdashboard;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.nightdeveloper.smartdashboard.model.ValCurs;
import com.github.nightdeveloper.smartdashboard.model.Valute;
import com.github.nightdeveloper.smartdashboard.service.ValuteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@SpringBootTest
public class ValuteServiceTest {

    private static final Logger logger = LogManager.getLogger(ValuteServiceTest.class);

    @Qualifier("webApplicationContext")
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ValuteService valuteService;
    
    @Test
    public void deserializationCBRTest() throws Exception {

        Resource resource = resourceLoader.getResource("classpath:cbr_test.xml");
        InputStream inputStream = resource.getInputStream();
        String content = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        Assertions.assertNotNull(content);

        XmlMapper xmlMapper = new XmlMapper();
        ValCurs valCurs = xmlMapper.readValue(content, ValCurs.class);

        Assertions.assertNotNull(valCurs);
        Assertions.assertNotNull(valCurs.getDate());
        Assertions.assertEquals("29.08.2020", valCurs.getDate());
        Assertions.assertEquals(LocalDate.parse("2020-08-29"), valuteService.parseDate(valCurs.getDate()));
        Assertions.assertNotNull(valCurs.getName());
        Assertions.assertEquals("Foreign Currency Market", valCurs.getName());
        Assertions.assertTrue(valCurs.getValute().size() > 0);

        Valute valute = valCurs.getValute().get(0);

        Assertions.assertNotNull(valute.getId());
        Assertions.assertEquals("R01010", valute.getId());
        Assertions.assertNotNull(valute.getNumCode());
        Assertions.assertEquals(36, valute.getNumCode());
        Assertions.assertNotNull(valute.getCharCode());
        Assertions.assertEquals("AUD", valute.getCharCode());
        Assertions.assertNotNull(valute.getNominal());
        Assertions.assertEquals(1, valute.getNominal());
        Assertions.assertNotNull(valute.getName());
        Assertions.assertEquals("Австралийский доллар", valute.getName());
        Assertions.assertNotNull(valute.getValue());
        Assertions.assertEquals("54,5904", valute.getValue());
        Assertions.assertEquals(new BigDecimal("54.5904"), valuteService.parseValue(valute.getValue()));
    }
}
