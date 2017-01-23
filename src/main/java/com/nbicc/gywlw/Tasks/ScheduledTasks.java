package com.nbicc.gywlw.Tasks;

import com.nbicc.gywlw.Service.ProjectService;
import com.nbicc.gywlw.Service.RefreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * Created by BigMao on 2016/12/16.
 */
@Component
public class ScheduledTasks {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private RefreshService refreshService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 1000*600) //间隔10min同步一次数据
    public void refreshData(){
        refreshService.refresh();
    }
}
