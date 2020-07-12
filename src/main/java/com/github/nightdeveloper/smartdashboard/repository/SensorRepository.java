package com.github.nightdeveloper.smartdashboard.repository;

import com.github.nightdeveloper.smartdashboard.entity.Sensor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SensorRepository extends MongoRepository<Sensor, String> {

    List<Sensor> findByDeviceId(String deviceId);
}


