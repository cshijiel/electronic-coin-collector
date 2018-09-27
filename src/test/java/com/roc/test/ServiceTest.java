package com.roc.test;

import com.roc.rest.HuobiRestApplication;
import com.roc.rest.autoconfigure.HuobiRestAutoConfiguration;
import com.roc.rest.entity.trade.KlineItem;
import com.roc.rest.service.KlineService;
import com.roc.rest.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HuobiRestApplication.class)
@Import(HuobiRestAutoConfiguration.class)
public class ServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    KlineService klineService;

    @Test
    public void test() {
        List<KlineItem> klineResponse = klineService.getKlineResponse("xrpusdt", "1min", 20);
        System.out.println(klineResponse);
    }

}
