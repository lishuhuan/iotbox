package com.nbicc.gywlw.Service;

import com.nbicc.gywlw.Model.*;
import com.nbicc.gywlw.mapper.*;
import com.nbicc.gywlw.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service

public class ManufacturerService {

	private static final Logger logger = LoggerFactory.getLogger(ManufacturerService.class);
	
	@Autowired
	private GywlwUserMapper gywlwUserMapper;
	
	@Autowired
	private GywlwDeviceMapper gywlwDeviceMapper;
	
	@Autowired
	private GywlwHistoryItemMapper gywlwHistoryItemMapper;

	@Autowired
	private GywlwDeviceGpioMapper gywlwDeviceGpioMapper;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private RefreshService refreshService;

	@Autowired
	private GywlwHistoryDataForGPIOMapper gywlwHistoryDataForGPIOMapper;
	
	
	public List<GywlwUser> searchUser(String factoryId,int level,String name){
		return gywlwUserMapper.searchUserByFactoy(factoryId,level,name);
	}
	
	public List<GywlwDevice> searchDevice(String adminId){
		return gywlwDeviceMapper.searchDeviceByFactory(adminId);
	}
	
	public List<GywlwHistoryItem> getHistoryData(String deviceId){
		refreshService.refresh(deviceId);
		List<GywlwHistoryItem> list = gywlwHistoryItemMapper.getHistoryData(deviceId);
		List<GywlwHistoryDataForGPIO> list1 = gywlwHistoryDataForGPIOMapper.getLatestData(deviceId);
		if(list1.size()>0){
			for(GywlwHistoryDataForGPIO gpio : list1){
				GywlwHistoryItem historyItem = new GywlwHistoryItem();
				historyItem.setDeviceId(deviceId);
				historyItem.setRegId(gpio.getGpioId());
				historyItem.setPlcName("GPIO");
				historyItem.setItemName(gywlwDeviceGpioMapper.selectByPrimaryKey(gpio.getGpioId()).getFieldName());
				historyItem.setItemAddress(gywlwDeviceGpioMapper.selectByPrimaryKey(gpio.getGpioId()).getFieldAddress());
				historyItem.setItemValue(gpio.getItemValue() * 1.0);
				historyItem.setItemId(gpio.getId());
				historyItem.setItemTime(gpio.getItemTime());
				list.add(historyItem);
			}
		}
		return list;
	}
	
	public List<GywlwHistoryItem> getDeviceAlarmlist(String startTime,String endTime,String deviceId,String severity){
		refreshService.refresh(deviceId);
		return gywlwHistoryItemMapper.getDeviceAlarmlist(MyUtil.timeTransformToString(startTime),
				MyUtil.timeTransformToString(endTime), deviceId,severity);

	}
	
	public GywlwHistoryItem getAlarmDetail(String itemId){
//		refreshService.refresh();
		return gywlwHistoryItemMapper.getAlarmDetail(itemId);
	}
	
	public List<GywlwUser> getFactoryLimitUser(String userId,String id){
		List<GywlwUser> list = gywlwUserMapper.getFactoryLimitUser(userId, id);
		GywlwUser admin = gywlwUserMapper.selectByPrimaryKey(userId);
		for(GywlwUser user : list){
			user.setContent(admin.getUserName());
		}
		return list;
	}

	@Transactional
	public Boolean editFactoryLimitUserDistribution(String factoryId,String userId,int tag){
		if(tag==0){
			GywlwUserAdminGroup adminGroup=new GywlwUserAdminGroup();
			adminGroup.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			adminGroup.setAdminId(userId);
			adminGroup.setUserId(factoryId);
			adminGroup.setCreateTime(new Date());
			adminGroup.setDelMark(0);
			gywlwUserMapper.addFactoryLimitUserDistribution(adminGroup);
		}
		else {
			gywlwUserMapper.deleteFactoryLimitUserDistribution(factoryId, userId);
		}
		return true;
	}

	public List<GywlwDevice> getFactoryDevicelist(String factoryId, String deviceSn, int level) {
		List<GywlwDevice> list = gywlwDeviceMapper.getFactoryDevicelist(factoryId, deviceSn,level);
		for(GywlwDevice device : list){
			device.setAdminName(gywlwUserMapper.selectByPrimaryKey(device.getAdminId()).getUserName());
		}
		return list;
	}

	public List<GywlwHistoryDataForGPIO> getDeviceAlarmlistForGpio(String startTime, String endTime, String deviceId, String severity) {
		refreshService.refresh(deviceId);
		return gywlwHistoryDataForGPIOMapper.getAlarmDataBySeverity(deviceId,severity,MyUtil.timeTransformToString(startTime),MyUtil.timeTransformToString(endTime));
	}
}
