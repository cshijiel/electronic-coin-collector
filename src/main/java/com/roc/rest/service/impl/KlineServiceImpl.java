package com.roc.rest.service.impl;

import com.roc.rest.client.impl.HuobiRestTemplateImpl;
import com.roc.rest.entity.Result;
import com.roc.rest.entity.trade.KlineItem;
import com.roc.rest.service.KlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author apple
 */
@Service
public class KlineServiceImpl implements KlineService {

    @Autowired
    private HuobiRestTemplateImpl restTemplate;

    @Override
    public List<KlineItem> getKlineResponse(String symbol, String period, Integer size) {
        Result<List<KlineItem>> orderHistoryResult = this.restTemplate.getForList("/market/history/kline?symbol={symbol}&period={period}&size={size}",
                new ParameterizedTypeReference<Result<List<KlineItem>>>() {
                }, symbol,period, size);
        Optional<List<KlineItem>> data = orderHistoryResult.getData();
        return data.get();
    }
}
