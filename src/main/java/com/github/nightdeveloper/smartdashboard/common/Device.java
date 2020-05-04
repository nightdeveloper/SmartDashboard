package com.github.nightdeveloper.smartdashboard.common;

public class Device<T> {

    private String id;
    private String name;
    private Class<?> wrappingClass;

    public Device(String id, String name, Class<?> wrappingClass) {
        this.id = id;
        this.name = name;
        this.wrappingClass = wrappingClass;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Class<?> getWrappingClass() {
        return wrappingClass;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", wrappingClass=" + wrappingClass +
                '}';
    }
}
