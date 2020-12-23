package com.github.nightdeveloper.smartdashboard.property;

public class ArchiveProperty {

    private String path;
    private String cron;

    public ArchiveProperty() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    @Override
    public String toString() {
        return "ArchiveProperty{" +
                "path='" + path + '\'' +
                ", cron='" + cron + '\'' +
                '}';
    }
}
