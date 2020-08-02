package com.github.nightdeveloper.smartdashboard.property;

public class CameraProperty {

    private String name;
    private String url;
    private String current;

    public CameraProperty() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "CameraProperty{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", current='" + current + '\'' +
                '}';
    }
}
