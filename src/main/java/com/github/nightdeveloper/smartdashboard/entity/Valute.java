package com.github.nightdeveloper.smartdashboard.entity;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

public class Valute {

    @Id
    protected String id;
    protected String charCode;
    protected BigDecimal value;
    protected Date date;

    public Valute() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ValuteEntity{" +
                "id='" + id + '\'' +
                ", charCode='" + charCode + '\'' +
                ", value=" + value +
                ", date=" + date +
                '}';
    }
}
