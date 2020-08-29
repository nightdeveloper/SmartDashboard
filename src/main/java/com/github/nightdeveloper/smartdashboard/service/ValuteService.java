package com.github.nightdeveloper.smartdashboard.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ValuteService {

    private static final Logger logger = LogManager.getLogger(ValuteService.class);

    private static final DateTimeFormatter valuteDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public LocalDate parseDate(String valuteDate) {

        if (valuteDate == null) {
            return null;
        }

        LocalDate parsedDate =  LocalDate.parse(valuteDate, valuteDateFormatter);

        logger.debug("parsed date " + parsedDate);

        return parsedDate;
    }

    public BigDecimal parseValue(String value) {
        if (value == null) {
            return null;
        }

        BigDecimal parsedValue = new BigDecimal(value.replace(",", "."));

        logger.debug("parsed value " + parsedValue);

        return parsedValue;
    }
}
