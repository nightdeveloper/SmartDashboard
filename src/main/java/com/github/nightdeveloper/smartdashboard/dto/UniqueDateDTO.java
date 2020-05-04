package com.github.nightdeveloper.smartdashboard.dto;

import java.time.LocalDateTime;

public class UniqueDateDTO {

    private LocalDateTime uniqueDate;

    public UniqueDateDTO() {
    }

    public LocalDateTime getUniqueDate() {
        return uniqueDate;
    }

    public void setUniqueDate(LocalDateTime uniqueDate) {
        this.uniqueDate = uniqueDate;
    }

    @Override
    public String toString() {
        return "UniqueDateDTO{" +
                "uniqueDate=" + uniqueDate +
                '}';
    }
}
