package com.nbicc.gywlw.Tasks;

import com.nbicc.gywlw.Service.ProjectService;
import com.nbicc.gywlw.Service.RefreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println("现在时间：" + dateFormat.format(new Date()));
    }
    @Scheduled(fixedRate = 1000*3600) //间隔1小时同步一次数据
    public void refreshData(){
        refreshService.refresh();
    }
}
