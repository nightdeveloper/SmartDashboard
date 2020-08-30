package com.github.nightdeveloper.smartdashboard.repository;

import com.github.nightdeveloper.smartdashboard.entity.Valute;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ValuteRepository extends MongoRepository<Valute, String> {

    List<Valute> findValuteEntitiesByCharCode(String charCode);
}
