package com.nbicc.gywlw.Service;

import com.nbicc.gywlw.Model.*;
import com.nbicc.gywlw.mapper.GywlwDeviceMapper;
import com.nbicc.gywlw.mapper.GywlwHistoryDataForGPIOMapper;
import com.nbicc.gywlw.mapper.GywlwHistoryItemMapper;
import com.nbicc.gywlw.mapper.GywlwUserMapper;
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
//		logger.info("开始同步数据： " + new Date());
		refreshService.refresh();
//		logger.info("同步数据完成，开始查表： " + new Date());
		List<GywlwHistoryItem> list = gywlwHistoryItemMapper.getHistoryData(deviceId);
		List<GywlwHistoryDataForGPIO> list1 = gywlwHistoryDataForGPIOMapper.getLatestData(deviceId);
		if(list1.size()>0){
			for(GywlwHistoryDataForGPIO gpio : list1){
				GywlwHistoryItem historyItem = new GywlwHistoryItem();
				historyItem.setDeviceId(deviceId);
				historyItem.setRegId(gpio.getGpioId());
				historyItem.setItemValue(gpio.getValue() * 1.0);
				historyItem.setItemId(gpio.getId());
				historyItem.setItemTime(gpio.getTime());
				list.add(historyItem);
			}
		}
//		logger.info("查表结束，返回数据： " + new Date());
		return list;
	}
	
	public List<GywlwHistoryItem> getDeviceAlarmlist(String startTime,String endTime,String deviceId,String severity){
		refreshService.refresh();
		return gywlwHistoryItemMapper.getDeviceAlarmlist(MyUtil.timeTransformToString(startTime),
				MyUtil.timeTransformToString(endTime), deviceId,severity);

	}
	
	public GywlwHistoryItem getAlarmDetail(String itemId){
		refreshService.refresh();
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
		refreshService.refresh();
		return gywlwHistoryDataForGPIOMapper.getAlarmDataBySeverity(deviceId,severity,MyUtil.timeTransformToString(startTime),MyUtil.timeTransformToString(endTime));
	}
}
