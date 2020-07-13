package com.github.nightdeveloper.smartdashboard.common;

public class Device<T> {

    private String id;
    private String name;
    private String location;
    private Class<?> wrappingClass;

    public Device(String id, String name, String location, Class<?> wrappingClass) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.wrappingClass = wrappingClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Class<?> getWrappingClass() {
        return wrappingClass;
    }

    public void setWrappingClass(Class<?> wrappingClass) {
        this.wrappingClass = wrappingClass;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", wrappingClass=" + wrappingClass +
                '}';
    }
}
