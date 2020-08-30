package com.github.nightdeveloper.smartdashboard.repository;

import com.github.nightdeveloper.smartdashboard.dto.UniqueDateDTO;
import com.github.nightdeveloper.smartdashboard.entity.Valute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Repository
public class ValuteAggregationRepository {

    private static final Logger logger = LogManager.getLogger(ValuteAggregationRepository.class);

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ValuteAggregationRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Valute> getValuteByPeriod(String charCode, int days) {
        return getValuteByPeriod(new ArrayList<String>() {{ add(charCode); }}, days);
    }

    public List<Valute> getValuteByPeriod(List<String> charCode, int days) {

        long timeStart = System.currentTimeMillis();

        LocalDateTime maxDate;
        LocalDateTime minDate;

        Aggregation getMaxDate = newAggregation(
                match(Criteria.where("charCode").in(charCode)),
                group().max("date").as("uniqueDate"),
                project("uniqueDate")
                        .andExclude("_id")
        );

        UniqueDateDTO maxDateDTO = mongoTemplate
                .aggregate(getMaxDate, "valute", UniqueDateDTO.class)
                .getUniqueMappedResult();

        logger.info("get max date = " + maxDateDTO +
                ", " + (System.currentTimeMillis() - timeStart) + " nano");

        if (maxDateDTO == null) {
            return new ArrayList<>();
        }

        maxDate = maxDateDTO.getUniqueDate();

        minDate = maxDate
                .minus(days, ChronoUnit.DAYS)
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        Aggregation selectAggregation = newAggregation(
                match(Criteria.where("charCode").in(charCode)
                        .and("date").gte(minDate)
                ),
                sort(Sort.Direction.ASC, "date")
        );

        AggregationResults<Valute> result = mongoTemplate
                .aggregate(selectAggregation, "valute", Valute.class);

        final List<Valute> resultsSpring = result.getMappedResults();

        logger.info("request run for " + (System.currentTimeMillis() - timeStart) + " nano (items: " + resultsSpring.size() + ")");

        return resultsSpring;
    }
}
