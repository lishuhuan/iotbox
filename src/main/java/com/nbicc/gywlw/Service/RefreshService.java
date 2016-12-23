package com.nbicc.gywlw.Service;

import com.nbicc.gywlw.mapper.GywlwDeviceMapper;
import com.nbicc.gywlw.mapper.GywlwHistoryItemMapper;
import com.nbicc.gywlw.mapper.GywlwPlcInfoMapper;
import com.nbicc.gywlw.mapper.GywlwRegInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by BigMao on 2016/12/23.
 */
public class RefreshService {
    private static final Logger logger = LoggerFactory.getLogger(RefreshService.class);
    @Autowired
    private GywlwDeviceMapper gywlwDeviceMapper;
    @Autowired
    private GywlwPlcInfoMapper gywlwPlcInfoMapper;
    @Autowired
    private GywlwHistoryItemMapper gywlwHistoryItemMapper;
    @Autowired
    private GywlwRegInfoMapper gywlwRegInfoMapper;

    public void refresh(){
        refreshData();
    }
    public void refreshData() {
    }



}
