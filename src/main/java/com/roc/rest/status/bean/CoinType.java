package com.roc.rest.status.bean;

public enum CoinType {
    BTC(4, 19558),
    XRPBTC(4, 19558),
    BCH(2, 4057),
    ETH(2, 1400),
    LTC(2, 331),
    XRP(4, 3.1981),
    DASH(2, 1461)
    //
    ;
    private Double ratio;
    private Double maxPrice;

    CoinType(Number ratio, Number maxPrice) {
        this.ratio = ratio.doubleValue();
        this.maxPrice = maxPrice.doubleValue();
    }

    public String getUsdtSymbol() {
        if (this == XRPBTC) {
            return XRPBTC.name().toLowerCase();
        }
        return this.name().toLowerCase() + "usdt";
    }

    public Double getRatio() {
        return ratio;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }
}