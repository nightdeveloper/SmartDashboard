package com.github.nightdeveloper.smartdashboard;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.nightdeveloper.smartdashboard.entity.Valute;
import com.github.nightdeveloper.smartdashboard.model.SingleValute;
import com.github.nightdeveloper.smartdashboard.model.ValCurs;
import com.github.nightdeveloper.smartdashboard.repository.ValuteAggregationRepository;
import com.github.nightdeveloper.smartdashboard.repository.ValuteRepository;
import com.github.nightdeveloper.smartdashboard.service.ValuteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class ValuteServiceTest {

    private static final Logger logger = LogManager.getLogger(ValuteServiceTest.class);

    public static String TEST_VALUTE_CHARCODE = "test";
    public static String TEST_VALUTE_CHARCODE_2 = "test2";

    @Qualifier("webApplicationContext")
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ValuteService valuteService;

    @Autowired
    private ValuteRepository valuteRepository;

    @Autowired
    private ValuteAggregationRepository valuteAggregationRepository;

    @AfterEach
    void clearTestData() {
        valuteRepository.deleteAll(valuteRepository.findValuteEntitiesByCharCode(null));
        valuteRepository.deleteAll(valuteRepository.findValuteEntitiesByCharCode(TEST_VALUTE_CHARCODE));
        valuteRepository.deleteAll(valuteRepository.findValuteEntitiesByCharCode(TEST_VALUTE_CHARCODE_2));
    }

    @BeforeEach
    void prepareTestData() {
        clearTestData();

        Valute data1 = new Valute();
        data1.setCharCode(TEST_VALUTE_CHARCODE);
        data1.setDate(new Date());
        data1.setValue(new BigDecimal("1.2345"));
        valuteRepository.save(data1);

        Valute data2 = new Valute();
        data2.setCharCode(TEST_VALUTE_CHARCODE);
        data2.setDate(Date.from(
                LocalDate.now()
                        .atStartOfDay()
                        .minus(1, ChronoUnit.DAYS)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()));
        data2.setValue(new BigDecimal("2.3456"));
        valuteRepository.save(data2);
    }

    private void assertValuteList(List<Valute> results) {
        Assertions.assertTrue(results.size() > 0);

        for (Valute result : results) {
            Assertions.assertNotNull(result.getId());
            Assertions.assertNotNull(result.getDate());
            Assertions.assertNotNull(result.getCharCode());
            Assertions.assertNotNull(result.getValue());
        }
    }

    @Test
    void testRepositorySaveAndGetOne() {

        Valute data1 = new Valute();
        data1.setCharCode(TEST_VALUTE_CHARCODE_2);
        data1.setDate(new Date());
        data1.setValue(new BigDecimal("3.4567"));
        valuteRepository.save(data1);

        List<Valute> valuteList =
                valuteAggregationRepository.getValuteByPeriod(TEST_VALUTE_CHARCODE_2, 30);

        assertValuteList(valuteList);

        Assertions.assertEquals(1, valuteList.size());

        Valute receivedData = valuteList.get(0);
        Assertions.assertEquals(data1.getCharCode(), receivedData.getCharCode());
        Assertions.assertEquals(data1.getDate(), receivedData.getDate());
        Assertions.assertEquals(data1.getValue(), receivedData.getValue());
    }

    @Test
    void testRepositorySaveAndGetMultiple() {

        Valute data1 = new Valute();
        data1.setCharCode(TEST_VALUTE_CHARCODE_2);
        data1.setDate(new Date());
        data1.setValue(new BigDecimal("4.5678"));
        valuteRepository.save(data1);

        List<Valute> valuteList =
                valuteAggregationRepository.getValuteByPeriod(
                        new ArrayList<String>() {{
                            add(TEST_VALUTE_CHARCODE);
                            add(TEST_VALUTE_CHARCODE_2);
                        }}, 30);

        assertValuteList(valuteList);

        AtomicInteger test1Count = new AtomicInteger(0);
        AtomicInteger test2Count = new AtomicInteger(0);

        valuteList.forEach(valute -> {
            if (TEST_VALUTE_CHARCODE.equals(valute.getCharCode())) {
                test1Count.incrementAndGet();
            } else if (TEST_VALUTE_CHARCODE_2.equals(valute.getCharCode())) {
                test2Count.incrementAndGet();
            }
        });


        Assertions.assertEquals(2, test1Count.get());
        Assertions.assertEquals(1, test2Count.get());
    }

    @Test
    void testRepositoryAddGetTest() {

        List<Valute> valuteList =
                valuteAggregationRepository.getValuteByPeriod(TEST_VALUTE_CHARCODE, 30);

        assertValuteList(valuteList);
    }

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

        SingleValute valute = valCurs.getValute().get(0);

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
