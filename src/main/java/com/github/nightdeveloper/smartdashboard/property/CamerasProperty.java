package com.github.nightdeveloper.smartdashboard.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix="cameras")
public class CamerasProperty {

    private ArchiveProperty archive;
    private List<CameraProperty> list;

    public CamerasProperty() {
    }

    public ArchiveProperty getArchive() {
        return archive;
    }

    public void setArchive(ArchiveProperty archive) {
        this.archive = archive;
    }

    public List<CameraProperty> getList() {
        return list;
    }

    public void setList(List<CameraProperty> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "CamerasProperty{" +
                "archive=" + archive +
                ", list=" + list +
                '}';
    }
}
