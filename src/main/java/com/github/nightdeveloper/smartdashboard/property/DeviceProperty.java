package com.github.nightdeveloper.smartdashboard.property;

public class DeviceProperty {
    private String id;
    private String location;
    private String name;
    private String className;

    public DeviceProperty() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "DeviceProperty{" +
                "id='" + id + '\'' +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}