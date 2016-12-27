package com.nbicc.gywlw.Service;

import com.nbicc.gywlw.Model.GywlwDevice;
import com.nbicc.gywlw.Model.GywlwHistoryItem;
import com.nbicc.gywlw.Model.GywlwUser;
import com.nbicc.gywlw.Model.GywlwUserAdminGroup;
import com.nbicc.gywlw.mapper.GywlwDeviceMapper;
import com.nbicc.gywlw.mapper.GywlwHistoryItemMapper;
import com.nbicc.gywlw.mapper.GywlwUserMapper;
import com.nbicc.gywlw.util.MyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	
	public List<GywlwUser> searchUser(String factoryId,int level,String name){
		return gywlwUserMapper.searchUserByFactoy(factoryId,level,name);
	}
	
	public List<GywlwDevice> searchDevice(String adminId){
		return gywlwDeviceMapper.searchDeviceByFactory(adminId);
	}
	
	public List<GywlwHistoryItem> getHistoryData(String deviceId){
		logger.info("开始同步数据： " + new Date());
		refreshService.refresh();
		logger.info("同步数据完成，开始查表： " + new Date());
		List<GywlwHistoryItem> list = gywlwHistoryItemMapper.getHistoryData(deviceId);
		logger.info("查表结束，返回数据： " + new Date());
		return list;

	}
	
	public List<GywlwHistoryItem> getDeviceAlarmlist(String startTime,String endTime,String deviceId){
		refreshService.refresh();
		return gywlwHistoryItemMapper.getDeviceAlarmlist(MyUtil.timeTransformToString(startTime),
				MyUtil.timeTransformToString(endTime), deviceId);
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
		// TODO Auto-generated method stub
		List<GywlwDevice> list = gywlwDeviceMapper.getFactoryDevicelist(factoryId, deviceSn,level);
		for(GywlwDevice device : list){
			device.setAdminName(gywlwUserMapper.selectByPrimaryKey(device.getAdminId()).getUserName());
		}
		return list;
	}

}
