package com.roc.rest.controller;

import com.roc.rest.status.bean.CoinType;
import com.roc.rest.status.task.AverageBalanceJob;
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
    public Map<CoinType, Double> hello() {
        return averageBalanceJob.currentRatio();
    }
}
