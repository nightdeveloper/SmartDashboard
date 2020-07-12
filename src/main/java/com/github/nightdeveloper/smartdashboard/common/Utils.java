package com.github.nightdeveloper.smartdashboard.common;

import com.github.nightdeveloper.smartdashboard.dto.AverageDeviceValueDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static LocalDateTime getDateFromExploded(AverageDeviceValueDTO valueDTO) {
        if (valueDTO == null) {
            return null;
        }

        return LocalDateTime.of(valueDTO.getYear(), valueDTO.getMonth(), valueDTO.getDay(),
                valueDTO.getHour(), valueDTO.getMinute());
    }

    public static Double sensorVoltageToPercentage(Double voltage) {

        if (voltage == null) {
            return null;
        }

        double percentage = 0.0;

        if (voltage < 2100) {
            percentage = 0.0;
        } else if (voltage < 2440) {
            percentage = 6 - ((2440 - voltage) * 6) / 340;
        } else if (voltage < 2740) {
            percentage = 18 - ((2740 - voltage) * 12) / 300;
        } else if (voltage < 2900) {
            percentage = 42 - ((2900 - voltage) * 24) / 160;
        } else if (voltage < 3000) {
            percentage = 100 - ((3000 - voltage) * 58) / 100;
        } else if (voltage >= 3000) {
            percentage = 100 + ((voltage - 3000) * 58) / 100;
        }

        return Math.ceil(percentage);
    }

    public static void batteryVoltageListToPercentageList(List<AverageDeviceValueDTO> list) {

        if (list == null) {
            return;
        }

        list.forEach(valueDTO -> valueDTO.setAverage(
                sensorVoltageToPercentage(valueDTO.getAverage()))
        );
    }

    public static List<AverageDeviceValueDTO> sortAverages(List<AverageDeviceValueDTO> list, SortDirection direction) {
        return list.stream().sorted((o1, o2) -> {
                    int result;

                    if (o1 == null && o2 == null) {
                        return 0;

                    } else if (o1 == null) {
                        result = -1;

                    } else if (o2 == null) {
                        result = 1;

                    } else {
                        result = o1.getDeviceId().compareTo(o2.getDeviceId());

                        if (result == 0) {
                            result = getDateFromExploded(o1).compareTo(getDateFromExploded(o2));
                        }
                    }

                    if (direction == SortDirection.ASC) {
                        return result;

                    } else {
                        return -result;
                    }
                }
        ).collect(Collectors.toList());
    }
}
