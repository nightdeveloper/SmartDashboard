package com.github.nightdeveloper.smartdashboard.moex.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class JsonCsvDTO {
    private Map<String, MetadataTypeDTO> metadata;
    private List<String> columns;
    private List<List<String>> data;
}
