package com.roc.rest.service.impl;/**
 * @author roc cshijiel@gmail.com
 * @date 2018/1/7 21:34
 */

import com.roc.rest.client.impl.HuobiRestTemplateImpl;
import com.roc.rest.entity.Account;
import com.roc.rest.entity.AccountCurrency;
import com.roc.rest.entity.Currency;
import com.roc.rest.entity.Result;
import com.roc.rest.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AccountServiceImpl implements AccountService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HuobiRestTemplateImpl restTemplate;

    @Override
    public List<Account> getAccounts() {
        Result<List<Account>> accounts = this.restTemplate.getForList("/v1/account/accounts", new ParameterizedTypeReference<Result<List<Account>>>() {
        });
        return accounts.getData().get();
    }

    @Override
    public Account findAccountByType(String type) {
        return this.getAccounts()
                .stream()
                .filter(t -> type.equals(t.getType()))
                .findFirst().orElseThrow(() -> new RuntimeException("get account id error"));
    }

    @Override
    public List<Currency> getAccountBalance(Long accountId) {
        Result<AccountCurrency> accountCurrencyResult = this.restTemplate.getForBean("/v1/account/accounts/{account-id}/balance", new ParameterizedTypeReference<Result<AccountCurrency>>() {
        }, accountId);
        AccountCurrency accountCurrency = accountCurrencyResult.getData().get();
        List<Currency> collect = accountCurrency.getList().stream().filter(
                currency -> Double.valueOf(currency.getBalance()) > 0.000001D
        ).collect(Collectors.toList());
        return collect;
    }

    @Override
    public String getAccountBalance(Long accountId, String currency) {
        Result<AccountCurrency> accountCurrencyResult = this.restTemplate.getForBean("/v1/account/accounts/{account-id}/balance", new ParameterizedTypeReference<Result<AccountCurrency>>() {
        }, accountId);

        AccountCurrency accountCurrency = accountCurrencyResult.getData().get();
        Optional<Currency> first = accountCurrency.getList().stream()
                .filter(t -> currency.equals(t.getCurrency()) && "trade".equals(t.getType()))
                .findFirst();
        return first.get().getBalance();
    }
}
