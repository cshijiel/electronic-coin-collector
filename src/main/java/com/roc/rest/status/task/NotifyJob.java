package com.roc.rest.status.task;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.roc.rest.service.MailService;
import com.roc.rest.status.bean.CoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author chenpeng
 */
@Component
public class NotifyJob {

    @Autowired
    private MailService mailService;

    @PostConstruct
    public void sayHello() {
        mailService.sendSimpleMail("745656593@qq.com", "ECC启动成功！", "OK");
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void statistics() {
        Map<CoinType, Double> currentRatio = AverageBalanceJob.currentRatio;
        AverageBalanceJob.currentRatio = Maps.newHashMap();
        Double currentSum = AverageBalanceJob.currentSum;
        double v = new BigDecimal(currentSum).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        mailService.sendSimpleMail("745656593@qq.com", "SUM: " + v + " USD", JSON.toJSONString(currentRatio));
    }

}
