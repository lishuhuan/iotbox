package com.nbicc.gywlw.util;

import com.nbicc.gywlw.Model.GywlwHistoryItem;
import com.nbicc.gywlw.mapper.GywlwHistoryItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by BigMao on 2016/12/1.
 */
@Service
public class InitDataForHistory {
    @Autowired
    GywlwHistoryItemMapper gywlwHistoryItemMapper;

    public void add(){
        GywlwHistoryItem gywlwHistoryItem = new GywlwHistoryItem();
        Random random = new Random();
        gywlwHistoryItem.setProjectId("1");
        new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int j=0; j < 3600;++j){
                        Date date = new Date();
                        gywlwHistoryItem.setDeviceId(String.valueOf(0));
                        gywlwHistoryItem.setItemTime(addISecond(date,j));
                        gywlwHistoryItem.setRegId(String.valueOf(0));
                        gywlwHistoryItem.setItemValue(random.nextDouble()*100);
                        gywlwHistoryItemMapper.insertSelective(gywlwHistoryItem);
                    }
                }
            }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int j=0; j < 3600;++j){
                    Date date = new Date();
                    gywlwHistoryItem.setDeviceId(String.valueOf(1));
                    gywlwHistoryItem.setItemTime(addISecond(date,j));
                    gywlwHistoryItem.setRegId(String.valueOf(1));
                    gywlwHistoryItem.setItemValue(random.nextDouble()*100);
                    gywlwHistoryItemMapper.insertSelective(gywlwHistoryItem);
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int j=0; j < 3600;++j){
                    Date date = new Date();
                    gywlwHistoryItem.setDeviceId(String.valueOf(2));
                    gywlwHistoryItem.setItemTime(addISecond(date,j));
                    gywlwHistoryItem.setRegId(String.valueOf(2));
                    gywlwHistoryItem.setItemValue(random.nextDouble()*100);
                    gywlwHistoryItemMapper.insertSelective(gywlwHistoryItem);
                }
            }
        }).start();

    }
    public static Date addISecond(Date date,int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, i);
        return calendar.getTime();
    }




}
