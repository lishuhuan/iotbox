package com.nbicc.gywlw.Tasks;

import com.nbicc.gywlw.Service.ProjectService;
import com.nbicc.gywlw.Service.RefreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by BigMao on 2016/12/16.
 */
@Component
public class ScheduledTasks {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private RefreshService refreshService;

    //间隔30min同步一次数据
    @Scheduled(fixedRate = 1000*1800)
    public void refreshData(){
        refreshService.refresh();
    }

//    @Scheduled(fixedRate = 1000*5)
//    public void printSecond(){
//        System.out.println("now time: " + System.currentTimeMillis());
//    }
}
