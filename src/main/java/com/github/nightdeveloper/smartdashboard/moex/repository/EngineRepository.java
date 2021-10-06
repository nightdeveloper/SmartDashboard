package com.github.nightdeveloper.smartdashboard.moex.repository;

import com.github.nightdeveloper.smartdashboard.moex.entity.Engine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EngineRepository extends CrudRepository<Engine, Long> {
}
