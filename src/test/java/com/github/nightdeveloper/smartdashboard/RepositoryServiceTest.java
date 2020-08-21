package com.github.nightdeveloper.smartdashboard;

import com.github.nightdeveloper.smartdashboard.dto.AverageDeviceValueDTO;
import com.github.nightdeveloper.smartdashboard.dto.BatteryStatusDTO;
import com.github.nightdeveloper.smartdashboard.entity.ComfortSensor;
import com.github.nightdeveloper.smartdashboard.entity.Sensor;
import com.github.nightdeveloper.smartdashboard.repository.AggregationRepository;
import com.github.nightdeveloper.smartdashboard.repository.SensorRepository;
import com.github.nightdeveloper.smartdashboard.service.SensorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
class RepositoryServiceTest {

  private static final Logger logger = LogManager.getLogger(RepositoryServiceTest.class);

  public static final Long DAY_MILLISECONDS = 24 * 60 * 60 * 1000L;

  @Autowired
  private SensorRepository sensorRepository;

  @Autowired
  private SensorService sensorService;

  @Autowired
  private AggregationRepository aggregationRepository;

  @Test
  void contextLoads() {
  }

  @AfterEach
  void clearTestData() {
    sensorRepository.deleteAll(sensorRepository.findByDeviceId(null));
    sensorRepository.deleteAll(sensorRepository.findByDeviceId("test1"));
    sensorRepository.deleteAll(sensorRepository.findByDeviceId("test2"));
    sensorRepository.deleteAll(sensorRepository.findByDeviceId("test_battery"));
  }

  //@BeforeEach
  void prepareTestData() {
    clearTestData();

    ComfortSensor data1 = new ComfortSensor();
    data1.setDeviceId("test1");
    data1.setTemperature(new BigDecimal("36.6"));
    data1.setHumidity(new BigDecimal("50"));
    data1.setPressure(850L);
    data1.setDate(new Date());
    data1.setLinkquality(77L);
    sensorRepository.save((Sensor) data1);

    data1 = new ComfortSensor();
    data1.setDeviceId("test1");
    data1.setTemperature(new BigDecimal("35.4"));
    data1.setHumidity(new BigDecimal("56"));
    data1.setBattery(25L);
    data1.setPressure(870L);
    data1.setDate(new Date());
    sensorRepository.save((Sensor) data1);

    ComfortSensor data2 = new ComfortSensor();
    data2.setDeviceId("test2");
    data2.setDate(new Date());
    sensorRepository.save((Sensor) data2);

    data2 = new ComfortSensor();
    data2.setDeviceId("test2");
    data2.setTemperature(new BigDecimal("77.77"));
    data2.setDate(new Date());
    sensorRepository.save((Sensor) data2);

    ComfortSensor data3 = new ComfortSensor();
    data3.setDeviceId("test_battery");
    data3.setDate(new Date(new Date().getTime() - 5 * DAY_MILLISECONDS));
    data3.setBattery(100L);
    sensorRepository.save((Sensor) data3);

    data3 = new ComfortSensor();
    data3.setDeviceId("test_battery");
    data3.setDate(new Date(new Date().getTime() - 4 * DAY_MILLISECONDS));
    data3.setBattery(80L);
    sensorRepository.save((Sensor) data3);

    data3 = new ComfortSensor();
    data3.setDeviceId("test_battery");
    data3.setDate(new Date(new Date().getTime() - 3 * DAY_MILLISECONDS));
    data3.setBattery(99L);
    sensorRepository.save((Sensor) data3);

    data3 = new ComfortSensor();
    data3.setDeviceId("test_battery");
    data3.setDate(new Date(new Date().getTime() - 2 * DAY_MILLISECONDS));
    data3.setBattery(90L);
    sensorRepository.save((Sensor) data3);
  }

  private void assertAverages(List<AverageDeviceValueDTO> results) {
    Assertions.assertTrue(results.size() > 0);

    for (AverageDeviceValueDTO result : results) {
      Assertions.assertNotNull(result.getDeviceId());
      Assertions.assertNotNull(result.getYear());
      Assertions.assertNotNull(result.getMonth());
      Assertions.assertNotNull(result.getDay());
      Assertions.assertNotNull(result.getHour());
      Assertions.assertNotNull(result.getMinute());
      Assertions.assertNotNull(result.getAverage());
    }
  }

  @Test
  void testLastTemperatures() {
    List<AverageDeviceValueDTO> results = sensorService.getLastTemperatures();
    assertAverages(results);
  }

  @Test
  void testLastHumidity() {
    List<AverageDeviceValueDTO> results = sensorService.getLastHumidity();
    assertAverages(results);
  }

  @Test
  void testLastPressure() {
    List<AverageDeviceValueDTO> results = sensorService.getLastPressure();
    assertAverages(results);
  }

  @Test
  void testLastLinkQuality() {
    List<AverageDeviceValueDTO> results = sensorService.getLastLinkQuality();
    assertAverages(results);
  }

  @Test
  void testBatteryStatus() {
    List<AverageDeviceValueDTO> resultsList = sensorService.getLastBattery();
    assertAverages(resultsList);

    Map<String, BatteryStatusDTO> results = sensorService.getBatteryStatus(resultsList);

    for (BatteryStatusDTO batteryStatusDTO : results.values()) {

      Assertions.assertNotNull(batteryStatusDTO.getDeviceId());

      if ("test_battery".equals(batteryStatusDTO.getDeviceId())) {
        Assertions.assertEquals(90.0, batteryStatusDTO.getCurrentValue());
        Assertions.assertNotNull(batteryStatusDTO.getCurrentValueDate());

        Assertions.assertEquals(99.0, batteryStatusDTO.getLastMaxValue());
        Assertions.assertNotNull(batteryStatusDTO.getLastMaxValueDate());

        Assertions.assertNotNull(batteryStatusDTO.getDateDischarge());
      }
    }
  }

  @Test
  void testSensorsRepository() {
    List<Sensor> result = sensorRepository.findAll();
    Assertions.assertTrue(result.size() > 0);

    for (Sensor data : result) {
      if (data instanceof ComfortSensor) {
        ComfortSensor cData = (ComfortSensor) data;
        Assertions.assertNotNull(cData.getDeviceId());
        if (cData.getDate() == null) {
          Assertions.assertNotNull(cData.getDate());
        }
      }
    }
  }
}
