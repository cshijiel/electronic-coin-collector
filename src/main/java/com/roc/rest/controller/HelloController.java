package com.roc.rest.controller;

import com.roc.rest.status.bean.CoinType;
import com.roc.rest.status.task.AverageBalanceJob;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author chenpeng
 */
@RestController
@RequestMapping("")
public class HelloController {

    @Autowired
    private AverageBalanceJob averageBalanceJob;

    @RequestMapping("")
    public ShortViewVO hello() {
        return ShortViewVO.builder()
                .coinRatio(averageBalanceJob.currentRatio())
                .sum(AverageBalanceJob.currentSum)
                .build();
    }

    @RequestMapping("kill")
    public String kill() {
        try {
            return "turn off";
        } finally {
            System.exit(0);
        }
    }

    @Data
    @Builder
    private static class ShortViewVO {
        private Double sum;
        private Map<CoinType, Double> coinRatio;
    }
}
