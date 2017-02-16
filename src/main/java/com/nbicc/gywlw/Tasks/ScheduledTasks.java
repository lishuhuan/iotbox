package com.nbicc.gywlw.Tasks;

import com.nbicc.gywlw.Model.GywlwDevice;
import com.nbicc.gywlw.Service.RefreshService;
import com.nbicc.gywlw.mapper.GywlwDeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by BigMao on 2016/12/16.
 * 定时任务，
 */
@Component
public class ScheduledTasks {

    @Autowired
    private RefreshService refreshService;
    @Autowired
    private GywlwDeviceMapper gywlwDeviceMapper;

    //间隔30min同步一次数据
    @Scheduled(initialDelay=1000*600, fixedRate = 1000*1800)
    public void refreshData(){
        List<GywlwDevice> deviceList = gywlwDeviceMapper.selectAll();
        if(deviceList.size() != 0) {
            for(GywlwDevice device : deviceList){
                refreshService.refresh(device.getDeviceId());  //// TODO: 2017/2/15 目前是单线程
            }
        }
    }

//    @Scheduled(fixedRate = 1000*5)
//    public void printSecond(){
//        System.out.println("now time: " + System.currentTimeMillis());
//    }
}
