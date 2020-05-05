package com.github.nightdeveloper.smartdashboard.service;

import com.github.nightdeveloper.smartdashboard.common.SortDirection;
import com.github.nightdeveloper.smartdashboard.common.Utils;
import com.github.nightdeveloper.smartdashboard.dto.AverageDeviceValueDTO;
import com.github.nightdeveloper.smartdashboard.dto.BatteryStatusDTO;
import com.github.nightdeveloper.smartdashboard.repository.AggregationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SensorService {

    private final AggregationRepository aggregationRepository;

    public SensorService(AggregationRepository aggregationRepository) {
        this.aggregationRepository = aggregationRepository;
    }

    private List<AverageDeviceValueDTO> roundAverages(List<AverageDeviceValueDTO> values) {
        values.forEach(valueDTO -> valueDTO.setAverage(
                BigDecimal.valueOf(valueDTO.getAverage()).setScale(0, RoundingMode.HALF_EVEN).doubleValue()
        ));
        return values;
    }

    private List<AverageDeviceValueDTO> distinct(List<AverageDeviceValueDTO> values) {

        List<AverageDeviceValueDTO> result = new ArrayList<>();

        Map<String, Double> prevValues = new HashMap<>();

        List<AverageDeviceValueDTO> sortedValues = Utils.sortAverages(values, SortDirection.ASC);

        for(int i=sortedValues.size()-1; i>=0; i--) {
            AverageDeviceValueDTO valueDTO = sortedValues.get(i);

            Double prev = prevValues.get(valueDTO.getDeviceId());

            if (prev == null ||
                    (valueDTO.getAverage() != null && valueDTO.getAverage().compareTo(prev) != 0)) {
                result.add(valueDTO);
            }

            prevValues.put(valueDTO.getDeviceId(), valueDTO.getAverage());
        }

        return result;
    }

    public List<AverageDeviceValueDTO> getLastTemperatures() {
        return distinct(roundAverages(aggregationRepository.getAverages("temperature", 1)));
    }

    public List<AverageDeviceValueDTO> getLastHumidity() {
        return distinct(roundAverages(aggregationRepository.getAverages("humidity", 1)));
    }

    public List<AverageDeviceValueDTO> getLastPressure() {
        List<AverageDeviceValueDTO> results = aggregationRepository.getAverages("pressure", 1);

        results.forEach(valueDTO -> valueDTO.setAverage((double) Math.round(valueDTO.getAverage() * 0.750062)));

        return distinct(roundAverages(results));
    }

    public List<AverageDeviceValueDTO> getLastBattery() {
        return distinct(roundAverages(aggregationRepository.getAverages("voltage", 90)));
    }

    public List<AverageDeviceValueDTO> getLastLinkQuality() {
        return distinct(roundAverages(aggregationRepository.getAverages("linkquality", 1)));
    }

    public Map<String, BatteryStatusDTO> getBatteryStatus(List<AverageDeviceValueDTO> values) {

        Map<String, BatteryStatusDTO> results = new HashMap<>();
        Map<String, Double> prevMaxValues = new HashMap<>();

        List<AverageDeviceValueDTO> sortedValues = Utils.sortAverages(values, SortDirection.ASC);

        for (AverageDeviceValueDTO value : sortedValues) {
            BatteryStatusDTO resultValue = results.get(value.getDeviceId());
            String deviceId = value.getDeviceId();
            LocalDateTime valueDateTime = Utils.getDateFromExploded(value);
            double valueDouble = value.getAverage();

            if (resultValue == null) {
                resultValue = new BatteryStatusDTO();
                resultValue.setDeviceId(deviceId);
                results.put(deviceId, resultValue);
            }

            if (resultValue.getCurrentValueDate() == null ||
                    resultValue.getCurrentValueDate().compareTo(valueDateTime) < 0) {

                resultValue.setCurrentValueDate(valueDateTime);
                resultValue.setCurrentValue(valueDouble);
            }

            Double prevValue = prevMaxValues.get(deviceId);
            if (prevValue == null ||
                    valueDouble > prevValue) {

                resultValue.setLastMaxValueDate(valueDateTime);
                resultValue.setLastMaxValue(valueDouble);
            }

            prevMaxValues.put(deviceId, valueDouble);
        }

        for (BatteryStatusDTO batteryStatusDTO : results.values()) {
            if (batteryStatusDTO.getCurrentValue() != null &&
                    batteryStatusDTO.getCurrentValueDate() != null &&
                    batteryStatusDTO.getLastMaxValue() != null &&
                    batteryStatusDTO.getLastMaxValueDate() != null) {

                Period period = Period.between(batteryStatusDTO.getLastMaxValueDate().toLocalDate(),
                        batteryStatusDTO.getCurrentValueDate().toLocalDate());

                if (!period.isZero() &&
                        !batteryStatusDTO.getLastMaxValue().equals(batteryStatusDTO.getCurrentValue())) {

                    Double dischargePerDay =
                            (double) period.getDays() /
                                    (batteryStatusDTO.getLastMaxValue() - batteryStatusDTO.getCurrentValue());

                    long daysLeft = (long) (batteryStatusDTO.getCurrentValue() * dischargePerDay);

                    batteryStatusDTO.setDaysLeft(daysLeft);
                    batteryStatusDTO.setDateDischarge(LocalDateTime.now().plus(daysLeft, ChronoUnit.DAYS));
                }
            }
        }

        return results;
    }
}
