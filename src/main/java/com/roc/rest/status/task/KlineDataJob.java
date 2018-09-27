package com.roc.rest.status.task;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.roc.rest.entity.trade.KlineItem;
import com.roc.rest.service.KlineService;
import com.roc.rest.status.bean.CoinType;
import com.roc.rest.status.bean.KlineStatus;
import com.roc.rest.status.bean.KlineTimeWindow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @author apple
 */
@Component
@Slf4j
public class KlineDataJob {

    public static Map<CoinType, LinkedList<KlineItem>> coin2KlineMinutesMap = Maps.newHashMap();
    public static Map<CoinType, Map<KlineTimeWindow, LinkedList<KlineItem>>> coinTypePeriod2KlineData = Maps.newHashMap();

    public static KlineStatus klineStatus = KlineStatus.FLUCTUATION;

    @Autowired
    private KlineService klineService;

    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 3)
    public void getKlineData() {
        int size = 1;
        for (CoinType coinType : CoinType.values()) {
            for (KlineTimeWindow klineTimeWindow : KlineTimeWindow.values()) {
                coinTypePeriod2KlineData.computeIfAbsent(coinType, k -> Maps.newHashMap());
                Map<KlineTimeWindow, LinkedList<KlineItem>> timeWindowLinkedListMap = coinTypePeriod2KlineData.get(coinType);
                LinkedList<KlineItem> klineItems = timeWindowLinkedListMap.get(klineTimeWindow);

                if (CollectionUtils.isEmpty(klineItems)) {
                    // 初始化Map
                    size = klineTimeWindow.getInitSize();
                    timeWindowLinkedListMap.put(klineTimeWindow, klineItems = Lists.newLinkedList());
                }
                List<KlineItem> klineResponse = klineService.getKlineResponse(coinType.getUsdtSymbol(), klineTimeWindow.getPeriod(), size);
                if (size == 1 && Objects.equals(klineResponse.get(0).getId(), klineItems.getFirst().getId())) {
                    continue;
                }
                klineItems.addAll(0, klineResponse);

                while (klineItems.size() > klineTimeWindow.getMaxSize()) {
                    klineItems.removeLast();
                }
            }
        }
    }

    @Scheduled(fixedDelay = 60 * 1000)
    public void calculateKlineStatus() {
        if (!checkDataIsAlready()) {
            return;
        }


        int timeWindow = 10;
        double compareValue = -0.07;
        Set<CoinType> plummetCoinTypes = Sets.newHashSet();
        for (CoinType coinType : CoinType.values()) {
            LinkedList<KlineItem> klineItems = coinTypePeriod2KlineData.get(coinType).get(KlineTimeWindow.OneMinitue);
            if (klineStatus == KlineStatus.PLUMMET) {
                // TODO: 2018/1/28 如果当前是急跌，则等待下跌超过37%后再进行判断；否则直接跳出循环
                Double high = this.getHighPriceIn(klineItems, timeWindow = klineItems.size());
                KlineItem first = klineItems.getFirst();
                double close = first.getClose();
                double ratio = (close - high) / high;

                //##.00%   百分比格式，后面不足2位的用0补齐
                DecimalFormat decimalFormat = new DecimalFormat("0.000%");
                String ratioString = decimalFormat.format(ratio);
                if (ratio < -0.37) {
                    log.warn("Coin {} MAX Ratio in {} minutes reach -37%:{}", coinType, timeWindow, ratioString);
                    log.info("开始定投");
                    klineStatus = KlineStatus.FLUCTUATION;
                }
            } else {
                Double high = this.getHighPriceIn(klineItems, timeWindow);

                KlineItem first = klineItems.getFirst();
                double close = first.getClose();
                double ratio = (close - high) / high;
                //##.00%   百分比格式，后面不足2位的用0补齐
                DecimalFormat decimalFormat = new DecimalFormat("0.000%");
                String ratioString = decimalFormat.format(ratio);
//                log.info("Coin {} MAX Ratio in {} minutes:{}", coinType, timeWindow, ratioString);

                if (ratio < compareValue) {
                    log.warn("Coin {} MAX Ratio in {} minutes reach -7%:{}", coinType, timeWindow, ratioString);
                    plummetCoinTypes.add(coinType);
                }
            }

        }
        if (plummetCoinTypes.size() > CoinType.values().length - 1) {
            klineStatus = KlineStatus.PLUMMET;
            log.warn("KlineStatus switch status to {}, CoinTypes is {}", klineStatus, plummetCoinTypes);
        }
    }

    private Double getHighPriceIn(List<KlineItem> subList, int minute) {
        return subList.subList(0, minute).stream().max(Comparator.comparingDouble(KlineItem::toMiddlePrice)).get().getHigh();
    }

    public static boolean checkDataIsAlready() {
        try {
            for (CoinType coinType : CoinType.values()) {
                for (KlineTimeWindow klineTimeWindow : KlineTimeWindow.values()) {
                    LinkedList<KlineItem> klineItems = coinTypePeriod2KlineData.get(coinType).get(klineTimeWindow);
                    if (CollectionUtils.isEmpty(klineItems)) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
