package com.roc.rest.entity.trade;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KlineItem {
    private Long id;
    private double open;
    private double close;
    private double low;
    private double high;
    private double amount;
    private double vol;
    private int count;

    public Double toMiddlePrice() {
        return ((open + close) * 0.7 + (low + high) * 0.3) / 2;
    }
}
