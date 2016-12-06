package com.nbicc.gywlw.Model;

/**
 * Created by BigMao on 2016/12/5.
 */
public class DataModel {
    private String time;

    private String alarm1;

    private String alarm1Value;

    private String alarm2;

    private String alarm2Value;

    private String name;

    private Double value;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAlarm1() {
        return alarm1;
    }

    public void setAlarm1(String alarm1) {
        this.alarm1 = alarm1;
    }

    public String getAlarm1Value() {
        return alarm1Value;
    }

    public void setAlarm1Value(String alarm1Value) {
        this.alarm1Value = alarm1Value;
    }

    public String getAlarm2() {
        return alarm2;
    }

    public void setAlarm2(String alarm2) {
        this.alarm2 = alarm2;
    }

    public String getAlarm2Value() {
        return alarm2Value;
    }

    public void setAlarm2Value(String alarm2Value) {
        this.alarm2Value = alarm2Value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
