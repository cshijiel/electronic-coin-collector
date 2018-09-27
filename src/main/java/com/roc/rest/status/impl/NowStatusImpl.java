package com.roc.rest.status.impl;

import com.roc.rest.status.NowStatus;
import com.roc.rest.status.bean.KlineStatus;
import org.springframework.stereotype.Service;

@Service
public class NowStatusImpl implements NowStatus {


    @Override
    public KlineStatus getKlineStatus() {
        return null;
    }
}
