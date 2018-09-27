package com.roc.rest.entity.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 已成功完成的订单
 *
 * @author roc cshijiel@gmail.com
 * @date 2018/1/7 15:29
 */
@Data
public class OrderHistory {
    private long id;
    @JsonProperty("order-id")
    private long orderId;
    @JsonProperty("match-id")
    private long matchId;
    private String symbol;
    private String type;
    private String source;
    private BigDecimal price;
    @JsonProperty("filled-amount")
    private BigDecimal filledAmount;
    @JsonProperty("filled-fees")
    private BigDecimal filledFees;
    @JsonProperty("created-at")
    private long createdAt;
}
