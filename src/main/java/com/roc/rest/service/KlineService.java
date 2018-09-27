package com.roc.rest.service;

import com.roc.rest.entity.trade.KlineItem;

import java.util.List;

/**
 * @author apple
 */
public interface KlineService {
    /**
     * 获取K线数据
     *
     * @param symbol 交易对
     * @param period 时间周期单位
     * @param size   数量
     * @return K线数据
     */
    List<KlineItem> getKlineResponse(String symbol, String period, Integer size);
}
