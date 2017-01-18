package com.nbicc.gywlw.Service;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.gywlw.Dao.LoginTicketDAO;
import com.nbicc.gywlw.Model.GywlwDevice;
import com.nbicc.gywlw.Model.GywlwUser;
import com.nbicc.gywlw.Model.HostHolder;
import com.nbicc.gywlw.Model.LoginTicket;
import com.nbicc.gywlw.mapper.*;
import com.nbicc.gywlw.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by BigMao on 2017/1/18.
 */
@Service
public class ApiService {
    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

    @Autowired
    private GywlwUserMapper gywlwUserMapper;
    @Autowired
    private GywlwProjectMapper gywlwProjectMapper;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private GywlwProjectUserGroupMapper gywlwProjectUserGroupMapper;
    @Autowired
    private GywlwVariableRegGroupMapper gywlwVariableRegGroupMapper;
    @Autowired
    private GywlwHistoryItemMapper gywlwHistoryItemMapper;
    @Autowired
    private GywlwDeviceMapper gywlwDeviceMapper;
    @Autowired
    private MessageService messageService;
    @Autowired
    private GywlwMessageMapper gywlwMessageMapper;
    @Autowired
    private GywlwVariableMapper gywlwVariableMapper;
    @Autowired
    private GywlwPlcInfoMapper gywlwPlcInfoMapper;
    @Autowired
    private GywlwRegInfoMapper gywlwRegInfoMapper;
    @Autowired
    private GywlwDataTrendMapper gywlwDataTrendMapper;
    @Autowired
    private GywlwProjectDeviceGroupMapper gywlwProjectDeviceGroupMapper;
    @Autowired
    private GywlwWarningRulesMapper gywlwWarningRulesMapper;
    @Autowired
    private RefreshService refreshService;
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public JSONObject deviceInfo(String token){
        GywlwUser gywlwUser;
        if(token != null){
            //解析token
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(token);
            if(loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0){
                return MyUtil.response(1,"请重新登录");
            }
            gywlwUser = gywlwUserMapper.selectByPrimaryKey(loginTicket.getUserId());
            //验证设备是否是该用户所有
            List<GywlwDevice> gywlwDevices = gywlwDeviceMapper.selectByAdminId(gywlwUser.getUserId());
            if(gywlwDevices.size() == 0){
                return MyUtil.response(2,"您未绑定任何工业物联网盒子");
            }
            List<GywlwDevice> newList = new ArrayList<>();
            for(GywlwDevice gywlwDevice : gywlwDevices){
                GywlwDevice device = new GywlwDevice();
                device.setHardwareEdition(gywlwDevice.getHardwareEdition());
                device.setSoftwareEdition(gywlwDevice.getSoftwareEdition());
                device.setDeviceSn(gywlwDevice.getDeviceSn());
                device.setThreeGMode(gywlwDevice.getThreeGMode());
                newList.add(device);
            }
            return MyUtil.response(0,newList);
        }
        return MyUtil.response(1,"请重新登录");
    }

//    public JSONObject plcInfo(String token, String plcId) {
//
//    }
}
