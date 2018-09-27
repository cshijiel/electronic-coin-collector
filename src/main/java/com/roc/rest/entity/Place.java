package com.roc.rest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author roc cshijiel@gmail.com
 * @date 2018/1/6 16:59
 */
@Data
@Builder
public class Place {
    @JsonProperty("account-id")
    private String accountId;
    private String amount;
    private String price;
    private String source;
    private String symbol;
    private String type;
}
