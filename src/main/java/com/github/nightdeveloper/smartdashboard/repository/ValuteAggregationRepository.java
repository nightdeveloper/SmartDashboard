package com.github.nightdeveloper.smartdashboard.repository;

import com.github.nightdeveloper.smartdashboard.constants.ValuteConst;
import com.github.nightdeveloper.smartdashboard.entity.Valute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class ValuteAggregationRepository {

    private static final Logger logger = LogManager.getLogger(ValuteAggregationRepository.class);

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ValuteAggregationRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Valute> getValuteByPeriod(String charCode, int days) {
        return getValuteByPeriodAndCharCodes(new ArrayList<String>() {{ add(charCode); }}, days);
    }

    public List<Valute> getValuteByPeriod(ValuteConst valute, int days) {
        return getValuteByPeriodAndCharCodes(new ArrayList<String>() {{ add(valute.getCode()); }}, days);
    }

    public List<Valute> getValuteByPeriod(List<ValuteConst> charCode, int days) {

        List<String> charCodes = charCode.stream()
                .map(ValuteConst::getCode)
                .collect(Collectors.toList());

        return getValuteByPeriodAndCharCodes(charCodes, days);
    }

    public List<Valute> getValuteByPeriodAndCharCodes(List<String> charCodes, int days) {

        long timeStart = System.currentTimeMillis();

        LocalDate maxDate;
        LocalDate minDate;

        Query maxDateQuery = new Query();
        maxDateQuery.addCriteria(Criteria.where("charCode").in(charCodes));
        maxDateQuery.with(Sort.by(Sort.Direction.DESC, "$natural"));
        maxDateQuery.limit(1);

        List<Valute> maxDateDTOs = mongoTemplate.find(maxDateQuery, Valute.class);
        maxDate = maxDateDTOs.size() == 1 ? maxDateDTOs.get(0).getDate() : null;

        logger.info("get max date = " + maxDate +
                ", " + (System.currentTimeMillis() - timeStart) + " nano");

        if (maxDate == null) {
            return new ArrayList<>();
        }

        minDate = maxDate
                .minus(days, ChronoUnit.DAYS);

        Aggregation selectAggregation = newAggregation(
                match(Criteria.where("charCode").in(charCodes)
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
