package com.github.nightdeveloper.smartdashboard.repository;

import com.github.nightdeveloper.smartdashboard.dto.AverageDeviceValueDTO;
import com.github.nightdeveloper.smartdashboard.dto.UniqueDateDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class AggregationRepository {

    private static final Logger logger = LogManager.getLogger(AggregationRepository.class);

    private final MongoTemplate mongoTemplate;

    @Autowired
    public AggregationRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<AverageDeviceValueDTO> getAverages(String field, int days) {

        Aggregation getMaxDate = newAggregation(
                match(Criteria.where(field).ne(null)),
                group().max("date").as("uniqueDate"),
                project("uniqueDate")
                        .andExclude("_id")
        );

        UniqueDateDTO maxDate = mongoTemplate
                .aggregate(getMaxDate, "sensor", UniqueDateDTO.class)
                .getUniqueMappedResult();

        logger.info("get max date for " + field + " = " + maxDate);

        if (maxDate == null) {
            return new ArrayList<>();
        }

        LocalDateTime minDate = maxDate.getUniqueDate()
                .minus(days, ChronoUnit.DAYS)
                .atZone(ZoneId.systemDefault()).toLocalDateTime();


        logger.info("get min date for " + field + " = " + minDate);

        Aggregation selectAggregation = newAggregation(
                match(Criteria.where(field).ne(null)
                .and("date").gte(minDate)
                ),
                project()
                        .and("deviceId").as("deviceId")
                        .and("date").extractYear().as("year")
                        .and("date").extractMonth().as("month")
                        .and("date").extractDayOfMonth().as("day")
                        .and("date").extractHour().as("hour")
                        .and("date").extractMinute().as("minute")
                       .and(ConvertOperators.ToDouble.toDouble("$" + field)).as("convertedValue")
                ,
                group("deviceId", "year", "month", "day", "hour", "minute")
                .avg("convertedValue").as("average"),
                sort(Sort.Direction.DESC, "deviceId", "year", "month", "day", "hour", "minute")
        );

        Long timeStart = System.currentTimeMillis();

        AggregationResults<AverageDeviceValueDTO> result = mongoTemplate
                .aggregate(selectAggregation, "sensor", AverageDeviceValueDTO.class);

        Long timeEnd = System.currentTimeMillis();

        logger.info("request run for " + (timeEnd - timeStart) + " nano");

        return result.getMappedResults();
    }

}
