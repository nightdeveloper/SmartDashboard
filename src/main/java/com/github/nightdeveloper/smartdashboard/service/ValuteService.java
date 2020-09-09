package com.github.nightdeveloper.smartdashboard.service;

import com.github.nightdeveloper.smartdashboard.constants.ValuteConst;
import com.github.nightdeveloper.smartdashboard.entity.Valute;
import com.github.nightdeveloper.smartdashboard.model.SingleValute;
import com.github.nightdeveloper.smartdashboard.model.ValCurs;
import com.github.nightdeveloper.smartdashboard.repository.ValuteRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValuteService {

    public static final String CBR_DAILY = "http://www.cbr.ru/scripts/XML_daily.asp";

    private static final Logger logger = LogManager.getLogger(ValuteService.class);

    private static final DateTimeFormatter valuteDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final List<ValuteConst> valuteToFetch = new ArrayList<ValuteConst>() {{
        add(ValuteConst.USD);
        add(ValuteConst.EUR);
    }};

    @Autowired
    private ValuteRepository valuteRepository;

    public ValCurs requestCurrentValute() throws RestClientException {

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(CBR_DAILY, ValCurs.class);
    }

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

    public void saveFavouriteValuteRate(ValCurs valCurs) {
        LocalDate date = parseDate(valCurs.getDate());

        List<SingleValute> result = valCurs.getValute().stream()
                .filter(singleValute -> valuteToFetch.contains(ValuteConst.from(singleValute.getCharCode())))
                .collect(Collectors.toList());

        if (result.size() > 0) {
            for(SingleValute singleValute : result) {

                List<Valute> existing =
                        valuteRepository.findValuteEntitiesByCharCodeAndDate(singleValute.getCharCode(), date);

                if (existing.size() > 0) {
                    logger.info("valute " + singleValute.getCharCode() + " exists for date " + date);
                    continue;
                }

                Valute valute = new Valute();
                valute.setDate(date);
                valute.setCharCode(singleValute.getCharCode());
                valute.setValue(parseValue(singleValute.getValue()));

                valuteRepository.save(valute);
                logger.info("valute " + singleValute.getCharCode() +
                        " saved for date " + date +
                        ", value = " + singleValute.getValue());
            }
        }
    }
}
