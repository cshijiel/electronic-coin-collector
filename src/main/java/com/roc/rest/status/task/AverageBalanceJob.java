package com.roc.rest.status.task;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.roc.rest.entity.Currency;
import com.roc.rest.entity.Place;
import com.roc.rest.service.AccountService;
import com.roc.rest.service.MailService;
import com.roc.rest.service.OrderService;
import com.roc.rest.status.bean.CoinType;
import com.roc.rest.status.bean.KlineTimeWindow;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.roc.rest.status.task.KlineDataJob.coinTypePeriod2KlineData;

/**
 * @author chenpeng
 */
@Component
@Slf4j
public class AverageBalanceJob {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MailService mailService;

    private static final Set<String> avgCoin = Sets.newHashSet(CoinType.BTC.name(), CoinType.XRP.name());

    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 10)
    public void averageStrategy() {

        if (!KlineDataJob.checkDataIsAlready()) {
            return;
        }

        // 1. 获取指定账户的XRP与BTC比例
        long accountId = accountService.findAccountByType("spot").getId();
        List<Currency> currencies = accountService.getAccountBalance(accountId);
        currencies = currencies.stream().filter(
                currency -> {
                    // Currency(currency=usdt, type=trade, balance=0.001795002580011747)
                    String name = currency.getCurrency().toUpperCase();
                    return !StringUtils.equalsIgnoreCase(name, "usdt")
                            && avgCoin.contains(name);
                }
        ).collect(Collectors.toList());

        // merge
        Map<String, Currency> mergeMap = Maps.newHashMap();
        for (Currency currency : currencies) {
            String currencyType = currency.getCurrency();
            if (mergeMap.containsKey(currencyType)) {
                Currency origin = mergeMap.get(currencyType);
                Double balance = Double.valueOf(origin.getBalance()) + Double.valueOf(currency.getBalance());

                Currency newCurrency = new Currency();
                newCurrency.setBalance(String.valueOf(balance));
                newCurrency.setCurrency(currencyType);
                newCurrency.setType("merge");
                mergeMap.put(currencyType, newCurrency);
            } else {
                mergeMap.put(currencyType, currency);
            }
        }

        currencies = Lists.newArrayList(mergeMap.values());

        double sum = currentSum = currencies.stream().mapToDouble(currency -> {
            Double balance = Double.valueOf(currency.getBalance());
            CoinType coinType = CoinType.valueOf(currency.getCurrency().toUpperCase());
            double close = coinTypePeriod2KlineData.get(coinType).get(KlineTimeWindow.OneMinitue).getFirst().getClose();
            return balance * close;
        }).sum();

        Map<CoinType, Currency> currencyMap = currencies.stream().collect(Collectors.toMap(
                currency -> CoinType.valueOf(currency.getCurrency().toUpperCase()),
                currency -> currency)
        );

        Map<CoinType, Double> collect = currencies.stream().collect(Collectors.toMap(
                currency -> CoinType.valueOf(currency.getCurrency().toUpperCase()),
                currency -> {
                    Double balance = Double.valueOf(currency.getBalance());
                    CoinType coinType = CoinType.valueOf(currency.getCurrency().toUpperCase());
                    double close = coinTypePeriod2KlineData.get(coinType).get(KlineTimeWindow.OneMinitue).getFirst().getClose();
                    return balance * close / sum;
                })
        );

        log.info("xrp btc map is {}", currentRatio = collect);
        for (Map.Entry<CoinType, Double> entry : collect.entrySet()) {
            double v = entry.getValue() - 0.55;
            if (v > 0) {
                CoinType coinType = entry.getKey();
                Currency currency = currencyMap.get(coinType);
                double sellCount = Double.valueOf(currency.getBalance()) * 0.035;
                swapCoin(accountId, coinType, sellCount);
            }
        }
    }

    private void swapCoin(Long accountId, CoinType coinType, double count) {
        int newScale = coinType == CoinType.BTC ? 6 : 0;
        String amount = String.valueOf(new BigDecimal(count).setScale(newScale, BigDecimal.ROUND_HALF_UP));
        log.info("sell {} , amount is {}", coinType, amount);


        String symbol = "xrpbtc";
        String type = "";
        switch (coinType) {
            case BTC:
                // 买入XRP
                type = "buy-market";
                break;
            case XRP:
                // 买入BTC，卖出XRP
                type = "sell-market";
                break;
            default:
                return;
        }
        if (StringUtils.isBlank(type)) {
            return;
        }
        Place place = Place.builder()
                .accountId(String.valueOf(accountId))
                .amount(amount)
                .symbol(symbol)
                .type(type)
                .build();
        mailService.sendSimpleMail("745656593@qq.com", "自动调仓，卖出" + coinType, place.toString());
        orderService.createOrder(place);
    }

    public static Map<CoinType, Double> currentRatio = Maps.newHashMap();
    public static Double currentSum = 0D;

    public Map<CoinType, Double> currentRatio() {
        return currentRatio;
    }
}
