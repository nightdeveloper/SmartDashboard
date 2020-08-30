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
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class SensorAggregationRepository {

    private static final Logger logger = LogManager.getLogger(SensorAggregationRepository.class);

    private final MongoTemplate mongoTemplate;

    @Autowired
    public SensorAggregationRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<AverageDeviceValueDTO> getAverages(String field, int days) {

        long timeStart;
        LocalDateTime maxDate;
        LocalDateTime minDate;

        /*
        timeStart = System.currentTimeMillis();

        MongoCollection<Document> sensorsCollection = mongoTemplate.getDb().getCollection("sensor");

        AggregateIterable<Document> maxDateAggregate = sensorsCollection.aggregate(
                Arrays.asList(
                        Aggregates.match(Filters.ne("date", null)),
                        Aggregates.group(null, Accumulators.max("_max", "$date"))
                )
        );

        Document maxDateDocument = maxDateAggregate.first();
        if (maxDateDocument == null) {
            return new ArrayList<>();
        }

        maxDate = ((Date) maxDateDocument.get("_max")).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        logger.info(field + " opt: get max date for " + field + " = " + maxDate +
                ", " + (System.currentTimeMillis() - timeStart) + " nano");

        minDate = maxDate
                .minus(days, ChronoUnit.DAYS)
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        logger.info(field + " opt: get min date for " + field + " = " + minDate);

        List<Bson> selectAggregateBSON = Arrays.asList(
                Aggregates.match(Filters.and(
                        Filters.ne(field, null),
                        Filters.gte("date", minDate
                                .atZone(ZoneId.systemDefault())
                                .toInstant())))

                , Aggregates.project(
                        Projections.fields(
                                Projections.excludeId(),
                                Projections.include("deviceId"),
                                Projections.computed("year",
                                        new Document("$year", "$date")),
                                Projections.computed("month",
                                        new Document("$month", "$date")),
                                Projections.computed("day",
                                        new Document("$dayOfMonth", "$date")),
                                Projections.computed("hour",
                                        new Document("$hour", "$date")),
                                Projections.computed("minute",
                                        new Document("$minute", "$date")),
                                Projections.computed("convertedValue",
                                        new Document("$toDouble", "$" + field))
                        )
                )

                , Aggregates.group(new Document("_id",
                                new Document("deviceId", "$deviceId")
                                        .append("year", "$year")
                                        .append("month", "$month")
                                        .append("day", "$day")
                                        .append("hour", "$hour")
                                        .append("minute", "$minute")
                        ),
                        Accumulators.avg("average", "$convertedValue"))

                , Aggregates.sort(Sorts.descending("deviceId", "year", "month", "day", "hour", "minute"))
        );

        AggregateIterable<Document> selectAggregate = sensorsCollection.aggregate(
                selectAggregateBSON
        );

        final List<AverageDeviceValueDTO> results = new ArrayList<>();

        selectAggregate.forEach((Consumer<? super Document>) document ->
        {
            Document keyDocument = ((Document)((Document)document.get("_id")).get("_id"));
            results.add(new AverageDeviceValueDTO() {{
                setDeviceId((String) keyDocument.get("deviceId"));
                setYear((Integer) keyDocument.get("year"));
                setMonth((Integer) keyDocument.get("month"));
                setDay((Integer) keyDocument.get("day"));
                setHour((Integer) keyDocument.get("hour"));
                setMinute((Integer) keyDocument.get("minute"));
                setAverage((Double) document.get("average"));
            }});
        });

        logger.info(field + " opt: request run for " + (System.currentTimeMillis() - timeStart) + " nano (items: " + results.size() + ")");
*/
        // -------------------------------------------------------------------

        timeStart = System.currentTimeMillis();

        Aggregation getMaxDate = newAggregation(
                match(Criteria.where(field).ne(null)),
                group().max("date").as("uniqueDate"),
                project("uniqueDate")
                        .andExclude("_id")
        );

        UniqueDateDTO maxDateDTO = mongoTemplate
                .aggregate(getMaxDate, "sensor", UniqueDateDTO.class)
                .getUniqueMappedResult();

        logger.info(field + " rep: get max date for " + field + " = " + maxDateDTO +
                ", " + (System.currentTimeMillis() - timeStart) + " nano");

        if (maxDateDTO == null) {
            return new ArrayList<>();
        }

        maxDate = maxDateDTO.getUniqueDate();

        minDate = maxDate
                .minus(days, ChronoUnit.DAYS)
                .atZone(ZoneId.systemDefault()).toLocalDateTime();


        logger.info(field + " rep: get min date for " + field + " = " + minDate);

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


        AggregationResults<AverageDeviceValueDTO> result = mongoTemplate
                .aggregate(selectAggregation, "sensor", AverageDeviceValueDTO.class);

        final List<AverageDeviceValueDTO> resultsSpring = result.getMappedResults();

        logger.info(field + " rep: request run for " + (System.currentTimeMillis() - timeStart) + " nano (items: " + resultsSpring.size() + ")");

        return resultsSpring;
    }

}
