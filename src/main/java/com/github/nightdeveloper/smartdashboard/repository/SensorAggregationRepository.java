package com.github.nightdeveloper.smartdashboard.repository;

import com.github.nightdeveloper.smartdashboard.dto.AverageDeviceValueDTO;
import com.github.nightdeveloper.smartdashboard.dto.IdResult;
import com.github.nightdeveloper.smartdashboard.entity.PlugSensor;
import com.github.nightdeveloper.smartdashboard.entity.Sensor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@Slf4j
public class SensorAggregationRepository {

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

        log.info(field + " opt: get max date for " + field + " = " + maxDate +
                ", " + (System.currentTimeMillis() - timeStart) + " nano");

        minDate = maxDate
                .minus(days, ChronoUnit.DAYS)
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        log.info(field + " opt: get min date for " + field + " = " + minDate);

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

        log.info(field + " opt: request run for " + (System.currentTimeMillis() - timeStart) + " nano (items: " + results.size() + ")");
*/
        // -------------------------------------------------------------------

        timeStart = System.currentTimeMillis();

        Query maxDateQuery = new Query();
        maxDateQuery.addCriteria(Criteria.where(field).ne(null));
        maxDateQuery.with(Sort.by(Sort.Direction.DESC, "$natural"));
        maxDateQuery.limit(1);

        List<Sensor> maxDateDTOs = mongoTemplate.find(maxDateQuery, Sensor.class);
        maxDate = maxDateDTOs.size() == 1 ? maxDateDTOs.get(0).getDate() : null;

        log.info(field + " rep: get max date for " + field + " = " + maxDate +
                ", " + (System.currentTimeMillis() - timeStart) + " nano");

        if (maxDate == null) {
            return new ArrayList<>();
        }

        minDate = maxDate
                .minus(days, ChronoUnit.DAYS)
                .atZone(ZoneId.systemDefault()).toLocalDateTime();


        log.info(field + " rep: get min date for " + field + " = " + minDate);

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
                project("deviceId", "year", "month", "day", "hour", "minute", "average"),
                sort(Sort.Direction.DESC, "deviceId", "year", "month", "day", "hour", "minute")
        );


        AggregationResults<AverageDeviceValueDTO> result = mongoTemplate
                .aggregate(selectAggregation, "sensor", AverageDeviceValueDTO.class);

        final List<AverageDeviceValueDTO> resultsSpring = result.getMappedResults();

        log.info(field + " rep: request run for " + (System.currentTimeMillis() - timeStart) + " nano (items: " + resultsSpring.size() + ")");

        return resultsSpring;
    }

    public List<PlugSensor> getSwitchLastStates() {

        long timeStart = System.currentTimeMillis();
        log.info("getting state devices list");

        Aggregation devicesListAggregation = newAggregation(
                match(Criteria.where("state").ne(null)),
                project("deviceId"),
                group("deviceId")
                );

        AggregationResults<IdResult> devicesListAggregationResult = mongoTemplate
                .aggregate(devicesListAggregation, "sensor", IdResult.class);


        final List<IdResult> devicesListResult = devicesListAggregationResult.getMappedResults();

        log.info("got " + (System.currentTimeMillis() - timeStart) + " nano (items: "
                + devicesListResult.size() + ")");

        List<PlugSensor> result = new ArrayList<>();

        for(IdResult deviceId : devicesListResult) {
            timeStart = System.currentTimeMillis();

            log.info("getting device state by id " + deviceId.get_id());

            Query lastStateQuery = new Query();
            lastStateQuery.addCriteria(Criteria.where("deviceId").is(deviceId.get_id()));
            lastStateQuery.with(Sort.by(Sort.Direction.DESC, "$natural"));
            lastStateQuery.limit(1);

            List<Sensor> resultAll = mongoTemplate.find(lastStateQuery, Sensor.class);

            for(Sensor sensor : resultAll) {
                if (sensor instanceof PlugSensor) {
                    result.add((PlugSensor) sensor);
                }
            }

            log.info("got " + (System.currentTimeMillis() - timeStart) + " nano (items: "
                    + devicesListResult.size() + ")");
        }

        return result;
    }
}
