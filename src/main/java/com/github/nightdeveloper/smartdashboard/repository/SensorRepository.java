package com.github.nightdeveloper.smartdashboard.repository;

import com.github.nightdeveloper.smartdashboard.entity.Sensor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

// {"temperature": { $gt : "0" }, "date": {$gte:new Date(new Date().setDate(new Date().getDate()-1))} }

public interface SensorRepository extends MongoRepository<Sensor, String> {

    List<Sensor> findByDeviceId(String deviceId);
}


