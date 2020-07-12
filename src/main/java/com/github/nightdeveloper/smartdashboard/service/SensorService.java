package com.github.nightdeveloper.smartdashboard.service;

import com.github.nightdeveloper.smartdashboard.common.SortDirection;
import com.github.nightdeveloper.smartdashboard.common.Utils;
import com.github.nightdeveloper.smartdashboard.dto.AverageDeviceValueDTO;
import com.github.nightdeveloper.smartdashboard.dto.BatteryStatusDTO;
import com.github.nightdeveloper.smartdashboard.repository.AggregationRepository;
import org.springframework.stereotype.Service;

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

    private List<AverageDeviceValueDTO> smooth(List<AverageDeviceValueDTO> values, int smoothCount) {

        List<AverageDeviceValueDTO> sortedValues = Utils.sortAverages(values, SortDirection.ASC);

        int itemsCountForDevice = 0;
        String currentDeviceId = null;
        List<AverageDeviceValueDTO> smoothedValues = new ArrayList<>();
        for (int i = 0; i < sortedValues.size(); i++) {

            AverageDeviceValueDTO currentValue = sortedValues.get(i);

            if (currentDeviceId == null || !currentDeviceId.equals(currentValue.getDeviceId())) {
                currentDeviceId = currentValue.getDeviceId();
                itemsCountForDevice = 0;
            }

            if (itemsCountForDevice > 1) {
                double avg = 0;

                int currentItemsForAverage = Math.min(itemsCountForDevice, smoothCount);

                for (int j = i - currentItemsForAverage + 1; j <= i; j++) {
                    avg += sortedValues.get(j).getAverage();
                }
                avg = avg / currentItemsForAverage;

                AverageDeviceValueDTO newValue = new AverageDeviceValueDTO(currentValue);
                newValue.setAverage(Math.floor(avg * 100) / 100);
                smoothedValues.add(newValue);

            } else {
                smoothedValues.add(currentValue);
            }

            itemsCountForDevice++;
        }

        List<AverageDeviceValueDTO> result = new ArrayList<>();
        for (int i = 0; i < smoothedValues.size(); i++) {
            AverageDeviceValueDTO valueDTO = smoothedValues.get(i);

            boolean isLastItem = i == smoothedValues.size() - 1;

            AverageDeviceValueDTO prevItem = i > 0 ? smoothedValues.get(i - 1) : null;
            AverageDeviceValueDTO nextItem = !isLastItem ? smoothedValues.get(i + 1) : null;

            if (prevItem == null || nextItem == null ||
                    prevItem.getDeviceId() == null || nextItem.getDeviceId() == null) {
                result.add(valueDTO);

            } else if (!prevItem.getDeviceId().equals(nextItem.getDeviceId())) {
                result.add(valueDTO);

            } else if (prevItem.getAverage() != null && nextItem.getAverage() != null &&
                            !prevItem.getAverage().equals(nextItem.getAverage())) {
                result.add(valueDTO);
            }
        }

        return result;
    }

    public List<AverageDeviceValueDTO> getLastTemperatures() {
        return aggregationRepository.getAverages("temperature", 1);
    }

    public List<AverageDeviceValueDTO> getLastHumidity() {
        return aggregationRepository.getAverages("humidity", 1);
    }

    public List<AverageDeviceValueDTO> getLastPressure() {
        List<AverageDeviceValueDTO> results = aggregationRepository.getAverages("pressure", 1);

        results.forEach(valueDTO -> valueDTO.setAverage((double) Math.round(valueDTO.getAverage() * 0.750062)));

        return smooth(results, 10);
    }

    public List<AverageDeviceValueDTO> getLastBattery() {
        return smooth(aggregationRepository.getAverages("voltage", 7), 20);
    }

    public List<AverageDeviceValueDTO> getLastLinkQuality() {
        return aggregationRepository.getAverages("linkquality", 1);
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
