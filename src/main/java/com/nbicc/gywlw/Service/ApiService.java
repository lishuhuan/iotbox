package com.nbicc.gywlw.Service;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.gywlw.Dao.LoginTicketDAO;
import com.nbicc.gywlw.Model.*;
import com.nbicc.gywlw.mapper.*;
import com.nbicc.gywlw.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by BigMao on 2017/1/18.
 * 对应BaseAPI
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
    private ProjectService projectService;
    @Autowired
    private ManufacturerService manufacturerService;
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
    @Autowired
    private GywlwDeviceOrderMapper gywlwDeviceOrderMapper;

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
                device.setDeviceName(gywlwDevice.getDeviceName());
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

    public JSONObject deviceStatus(String token, String deviceId) {
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
                device.setDeviceStatus(gywlwDevice.getDeviceStatus());
                device.setDeviceName(gywlwDevice.getDeviceName());
                if("3".equals(gywlwDevice.getDeviceStatus())) {
                    GywlwDeviceOrder gywlwDeviceOrder = gywlwDeviceOrderMapper.selectByDeviceId(gywlwDevice.getDeviceId());
                    device.setOrderCode(gywlwDeviceOrder.getOrderCode());
                    device.setOrderTotal(gywlwDeviceOrder.getOrderTotal());
                }
                newList.add(device);
            }
            return MyUtil.response(0,newList);
        }
        return MyUtil.response(1,"请重新登录");
    }

    @Transactional
    public JSONObject uploadOrderInfo(String token, String deviceId, String orderCode, String orderNum) {
        JSONObject result = checkAdminAndDevice(token,deviceId);
        if(result != null){
            return result;
        }
        GywlwDeviceOrder gywlwDeviceOrder = gywlwDeviceOrderMapper.selectByDeviceId(deviceId);
        if(gywlwDeviceOrder == null) {
            GywlwDeviceOrder order = new GywlwDeviceOrder();
            order.setDeviceId(deviceId);
            order.setDeviceSn(gywlwDeviceMapper.selectByDeviceId(deviceId).getDeviceSn());
            order.setOrderTotal(Integer.parseInt(orderNum));
            order.setOrderCode(orderCode);
            order.setCreatedTime(new Date());
            gywlwDeviceOrderMapper.insertSelective(order);
        }else{
            gywlwDeviceOrder.setOrderCode(orderCode);
            gywlwDeviceOrder.setOrderTotal(Integer.parseInt(orderNum));
            gywlwDeviceOrderMapper.updateByPrimaryKeySelective(gywlwDeviceOrder);
        }

        GywlwDevice device1 = new GywlwDevice();
        device1.setDeviceId(deviceId);
        device1.setDeviceStatus("3");
        gywlwDeviceMapper.updateByPrimaryKeySelective(device1);
        return MyUtil.response(0,"上传订单信息成功！");
    }

    public JSONObject regInfo(String token, String deviceId){
        JSONObject result = checkAdminAndDevice(token,deviceId);
        if(result != null){
            return result;
        }
        List<GywlwRegInfo> list = new ArrayList<>();
//      gywlwRegInfoMapper.selectByPlcId(gywlwPlcInfoMapper.selectByDeviceId1(deviceId).getId());
        List<GywlwPlcInfo> plcInfoList = gywlwPlcInfoMapper.selectByDeviceId(deviceId);
        for(GywlwPlcInfo plcInfo : plcInfoList){
            if(gywlwRegInfoMapper.selectByPlcId(plcInfo.getId()) != null) {
                list.addAll(gywlwRegInfoMapper.selectByPlcId(plcInfo.getId()));
            }
        }
        for(GywlwRegInfo reginfo : list){
            GywlwHistoryItem item = gywlwHistoryItemMapper.getDataForRegId(reginfo.getRegId());
            if(item == null){
                continue;
            }
            reginfo.setValue(item.getItemValue());
            reginfo.setLastConnect(item.getItemTime());
        }
        return MyUtil.response(0,list);
    }

    public JSONObject getRules(String token, String deviceId) {
        JSONObject result = checkAdminAndDevice(token,deviceId);
        if(result == null){
            return MyUtil.response(0,gywlwWarningRulesMapper.selectByDeviceId(deviceId));
        }
        return result;
    }

    public JSONObject checkAdminAndDevice(String token, String deviceId) {
        if(token != null) {
            //解析token
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(token);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                return MyUtil.response(1, "请重新登录");
            }
            GywlwUser gywlwUser = gywlwUserMapper.selectByPrimaryKey(loginTicket.getUserId());
            hostHolder.setGywlwUser(gywlwUser);
            GywlwDevice gywlwDevice = gywlwDeviceMapper.selectByDeviceId(deviceId);
            if(gywlwDevice == null){
                return MyUtil.response(1,"找不到该设备");
            }else {
                if (gywlwUser.getUserId().equals(gywlwDevice.getAdminId())) {
                    return null;
                }
                else{
                    return MyUtil.response(1,"无权访问该设备");
                }
            }

        }
        return MyUtil.response(1, "请重新登录");

    }

    public JSONObject plcInfo(String token, String deviceId) {
        JSONObject result = checkAdminAndDevice(token,deviceId);
        if(result == null){
            //只提供必要的信息，其他的不显示
            List<GywlwPlcInfo> list1 = new ArrayList<>();
            List<GywlwPlcInfo> list = gywlwPlcInfoMapper.selectByDeviceId(deviceId);
            if(list.size()>0){
                for(GywlwPlcInfo plcinfo: list){
                    GywlwPlcInfo plc = new GywlwPlcInfo();
                    plc.setPlcName(plcinfo.getPlcName());
                    plc.setPlcType(plcinfo.getPlcType());
                    plc.setDeviceId(plcinfo.getDeviceId());
                    plc.setPlcBrand(plcinfo.getPlcBrand());
                    list1.add(plc);
                }
            }
            return MyUtil.response(0,list1);
        }
        return result;
    }

    @Transactional
    public void testTransaction(){
        GywlwPlcInfo info = new GywlwPlcInfo();
        info.setId("233");
        gywlwPlcInfoMapper.insertSelective(info);
        int y = Integer.parseInt("a");
    }
}
