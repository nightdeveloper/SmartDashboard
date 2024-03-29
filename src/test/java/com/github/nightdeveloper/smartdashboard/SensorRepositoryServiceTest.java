package com.github.nightdeveloper.smartdashboard;

import com.github.nightdeveloper.smartdashboard.constants.Profiles;
import com.github.nightdeveloper.smartdashboard.dto.AverageDeviceValueDTO;
import com.github.nightdeveloper.smartdashboard.dto.BatteryStatusDTO;
import com.github.nightdeveloper.smartdashboard.entity.ComfortSensor;
import com.github.nightdeveloper.smartdashboard.entity.PlugSensor;
import com.github.nightdeveloper.smartdashboard.entity.Sensor;
import com.github.nightdeveloper.smartdashboard.repository.SensorRepository;
import com.github.nightdeveloper.smartdashboard.service.SensorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles(profiles = Profiles.TEST)
@Slf4j
class SensorRepositoryServiceTest {

  @Autowired
  private SensorRepository sensorRepository;

  @Autowired
  private SensorService sensorService;

  @Test
  void contextLoads() {
    // just test context loads
  }

  @AfterEach
  void clearTestData() {
    sensorRepository.deleteAll(sensorRepository.findByDeviceId(null));
    sensorRepository.deleteAll(sensorRepository.findByDeviceId("test1"));
    sensorRepository.deleteAll(sensorRepository.findByDeviceId("test2"));
    sensorRepository.deleteAll(sensorRepository.findByDeviceId("test_battery"));
    sensorRepository.deleteAll(sensorRepository.findByDeviceId("test_plug"));
  }

  @BeforeEach
  void prepareTestData() {
    clearTestData();

    ComfortSensor data1 = new ComfortSensor();
    data1.setDeviceId("test1");
    data1.setTemperature(new BigDecimal("36.6"));
    data1.setHumidity(new BigDecimal("50"));
    data1.setPressure(850L);
    data1.setDate(LocalDateTime.now());
    data1.setLinkquality(77L);
    sensorRepository.save((Sensor) data1);

    data1 = new ComfortSensor();
    data1.setDeviceId("test1");
    data1.setTemperature(new BigDecimal("35.4"));
    data1.setHumidity(new BigDecimal("56"));
    data1.setBattery(25L);
    data1.setPressure(870L);
    data1.setDate(LocalDateTime.now());
    sensorRepository.save((Sensor) data1);

    ComfortSensor data2 = new ComfortSensor();
    data2.setDeviceId("test2");
    data2.setDate(LocalDateTime.now());
    sensorRepository.save((Sensor) data2);

    data2 = new ComfortSensor();
    data2.setDeviceId("test2");
    data2.setTemperature(new BigDecimal("77.77"));
    data2.setDate(LocalDateTime.now());
    sensorRepository.save((Sensor) data2);

    ComfortSensor data3 = new ComfortSensor();
    data3.setDeviceId("test_battery");
    data3.setDate(LocalDateTime.now().minus(5, ChronoUnit.DAYS));
    data3.setBattery(100L);
    sensorRepository.save((Sensor) data3);

    data3 = new ComfortSensor();
    data3.setDeviceId("test_battery");
    data3.setDate(LocalDateTime.now().minus(4, ChronoUnit.DAYS));
    data3.setBattery(80L);
    sensorRepository.save((Sensor) data3);

    data3 = new ComfortSensor();
    data3.setDeviceId("test_battery");
    data3.setDate(LocalDateTime.now().minus(3, ChronoUnit.DAYS));
    data3.setBattery(99L);
    data3.setVoltage(5100L);
    sensorRepository.save((Sensor) data3);

    data3 = new ComfortSensor();
    data3.setDeviceId("test_battery");
    data3.setDate(LocalDateTime.now().minus(2, ChronoUnit.DAYS));
    data3.setBattery(90L);
    data3.setVoltage(5000L);
    sensorRepository.save((Sensor) data3);

    PlugSensor data4 = new PlugSensor();
    data4.setDeviceId("test_plug");
    data4.setDate(LocalDateTime.now());
    data4.setState("ON");
    sensorRepository.save((Sensor) data4);
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
        Assertions.assertEquals(5000.0, batteryStatusDTO.getCurrentValue());
        Assertions.assertNotNull(batteryStatusDTO.getCurrentValueDate());

        Assertions.assertEquals(5100.0, batteryStatusDTO.getLastMaxValue());
        Assertions.assertNotNull(batteryStatusDTO.getLastMaxValueDate());

        Assertions.assertNotNull(batteryStatusDTO.getDateDischarge());
      }
    }
  }

  @Test
  void testSwitchesStatus() {
    List<PlugSensor> resultsList = sensorService.getPlugsState();

    for(PlugSensor switchStateDTO : resultsList) {
      Assertions.assertNotNull(switchStateDTO.getDeviceId());
      Assertions.assertNotNull(switchStateDTO.getState());
      Assertions.assertNotNull(switchStateDTO.getDate());
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
        Assertions.assertNotNull(cData.getDate());
      }
    }
  }
}
