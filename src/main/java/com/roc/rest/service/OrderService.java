package com.roc.rest.service;

import com.roc.rest.entity.Place;
import com.roc.rest.entity.trade.OrderHistory;

import java.util.List;
import java.util.Optional;

/**
 * 交易订单相关服务接口
 *
 * @author roc cshijiel@gmail.com
 * @date 2018/1/7 15:57
 */
public interface OrderService {
    /**
     * 查询最后一次订单成交记录
     *
     * @param symbol 交易对
     * @return 最后一次交易成功结果
     */
    Optional<OrderHistory> findLatestOrderHistory(String symbol);

    List<OrderHistory> getHistoryOrderResponse(String symbol, int size);

    /**
     * 创建订单
     *
     * @param place 订单参数
     * @return 订单号
     */
    Optional<String> createOrder(Place place);

    /**
     * 撤消一个订单
     *
     * @param orderId
     * @return
     */
    Optional<String> submitcancel(String orderId);
}
