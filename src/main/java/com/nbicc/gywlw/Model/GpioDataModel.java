package com.nbicc.gywlw.Model;


/**
 * Created by BigMao on 2016/12/26.
 */
public class GpioDataModel {

    private Gpio gpio_1;
    private Gpio gpio_2;
    private Gpio gpio_3;
    private Gpio gpio_4;
    private Gpio gpio_5;
    private Gpio gpio_6;
    private Gpio gpio_7;
    private Gpio gpio_8;

    private String timestamp;


    public static class Gpio {
        public Integer alarm;
        public Integer value;
        public Integer alarm1;
        public Integer alarm2;

        public Integer getAlarm() {
            return alarm;
        }

        public void setAlarm(Integer alarm) {
            this.alarm = alarm;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public Integer getAlarm1() {
            return alarm1;
        }

        public void setAlarm1(Integer alarm1) {
            this.alarm1 = alarm1;
        }

        public Integer getAlarm2() {
            return alarm2;
        }

        public void setAlarm2(Integer alarm2) {
            this.alarm2 = alarm2;
        }
    }

    public Gpio getGpio_3() {
        return gpio_3;
    }

    public void setGpio_3(Gpio gpio_3) {
        this.gpio_3 = gpio_3;
    }

    public Gpio getGpio_7() {
        return gpio_7;
    }

    public void setGpio_7(Gpio gpio_7) {
        this.gpio_7 = gpio_7;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Gpio getGpio_1() {
        return gpio_1;
    }

    public void setGpio_1(Gpio gpio_1) {
        this.gpio_1 = gpio_1;
    }

    public Gpio getGpio_2() {
        return gpio_2;
    }

    public void setGpio_2(Gpio gpio_2) {
        this.gpio_2 = gpio_2;
    }

    public Gpio getGpio_4() {
        return gpio_4;
    }

    public void setGpio_4(Gpio gpio_4) {
        this.gpio_4 = gpio_4;
    }

    public Gpio getGpio_5() {
        return gpio_5;
    }

    public void setGpio_5(Gpio gpio_5) {
        this.gpio_5 = gpio_5;
    }

    public Gpio getGpio_6() {
        return gpio_6;
    }

    public void setGpio_6(Gpio gpio_6) {
        this.gpio_6 = gpio_6;
    }

    public Gpio getGpio_8() {
        return gpio_8;
    }

    public void setGpio_8(Gpio gpio_8) {
        this.gpio_8 = gpio_8;
    }
}

