package com.github.nightdeveloper.smartdashboard.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class SingleValute {

    @JacksonXmlProperty(localName = "ID", isAttribute = true)
    private String id;

    @JacksonXmlProperty(localName = "NumCode")
    private Long numCode;

    @JacksonXmlProperty(localName = "CharCode")
    private String charCode;

    @JacksonXmlProperty(localName = "Nominal")
    private Long nominal;

    @JacksonXmlProperty(localName = "Name")
    private String name;

    @JacksonXmlProperty(localName = "Value")
    private String value;

    public SingleValute() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getNumCode() {
        return numCode;
    }

    public void setNumCode(Long numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public Long getNominal() {
        return nominal;
    }

    public void setNominal(Long nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Valute{" +
                "id='" + id + '\'' +
                ", numCode=" + numCode +
                ", charCode='" + charCode + '\'' +
                ", nominal=" + nominal +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
