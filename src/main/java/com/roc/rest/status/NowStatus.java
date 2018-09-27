package com.roc.rest.status;

import com.roc.rest.status.bean.KlineStatus;

/**
 * @author apple
 */
public interface NowStatus {
    /**
     * 非必须，真正的K线数据才是必须的
     *
     * @return K线数据状态
     */
    KlineStatus getKlineStatus();
}
