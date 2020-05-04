package com.github.nightdeveloper.smartdashboard.common;

import com.github.nightdeveloper.smartdashboard.dto.AverageDeviceValueDTO;

import java.time.LocalDateTime;
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
                        result = getDateFromExploded(o1).compareTo(getDateFromExploded(o2));
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
