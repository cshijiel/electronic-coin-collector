package com.roc.rest.status.bean;

public enum KlineTimeWindow {
    OneMinitue("1min", 1000, 2000),
    FiveMinute("5min", 200, 400),
    FifteenMinute("15min", 60, 150),
    HalfAnHour("30min", 60, 150),
    Hour("60min", 60, 150),
    Day("1day", 30, 60),
    //
    ;

    private String period;
    private int initSize;
    private int maxSize;

    KlineTimeWindow(String period, int initSize, int maxSize) {
        this.period = period;
        this.initSize = initSize;
        this.maxSize = maxSize;
    }

    public String getPeriod() {
        return period;
    }

    public int getInitSize() {
        return initSize;
    }

    public int getMaxSize() {
        return maxSize;
    }
}