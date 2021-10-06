package com.github.nightdeveloper.smartdashboard.moex.model;

import lombok.Data;

@Data
public class IndexDTO {
    private JsonCsvDTO engines;
    private JsonCsvDTO markets;
    private JsonCsvDTO boards;
    private JsonCsvDTO boardgroups;
    private JsonCsvDTO durations;
    private JsonCsvDTO securitytypes;
    private JsonCsvDTO securitygroups;
    private JsonCsvDTO securitycollections;
}
