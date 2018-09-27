package com.roc.rest.service;

import com.roc.rest.entity.Account;
import com.roc.rest.entity.Currency;

import java.util.List;

/**
 * 账户信息相关接口
 *
 * @author roc cshijiel@gmail.com
 * @date 2018/1/7 21:27
 */
public interface AccountService {

    /**
     * 获取所有账户信息
     *
     * @return
     */
    List<Account> getAccounts();

    /**
     * 根据类型查询账户
     *
     * @param type 账户类型
     * @return
     */
    Account findAccountByType(String type);

    /**
     * 获取账户余额
     *
     * @param accountId 账户id
     * @return
     */
    List<Currency> getAccountBalance(Long accountId);

    /**
     * 获取账户余额
     *
     * @param accountId 账户id
     * @param currency  币种
     * @return
     */
    String getAccountBalance(Long accountId, String currency);

}
