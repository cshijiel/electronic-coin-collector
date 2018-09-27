package com.roc.rest.status.bean;

/**
 * @author apple
 */
public enum KlineStatus {
    // 快速下跌信号
    PLUMMET("急跌"),
    FLUCTUATION("波动")
    //
    ;

    private String description;

    KlineStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
