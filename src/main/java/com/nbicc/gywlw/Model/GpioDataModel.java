package com.nbicc.gywlw.Model;


/**
 * Created by BigMao on 2016/12/26.
 */
public class GpioDataModel {
    private String time;

    private String timestamp;

    private Gpio gpio;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Gpio getGpio() {
        return gpio;
    }

    public void setGpio(Gpio gpio) {
        this.gpio = gpio;
    }
}

