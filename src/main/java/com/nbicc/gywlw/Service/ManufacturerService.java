package com.nbicc.gywlw.Service;

import com.nbicc.gywlw.Model.GywlwDevice;
import com.nbicc.gywlw.Model.GywlwHistoryItem;
import com.nbicc.gywlw.Model.GywlwUser;
import com.nbicc.gywlw.Model.GywlwUserAdminGroup;
import com.nbicc.gywlw.mapper.GywlwDeviceMapper;
import com.nbicc.gywlw.mapper.GywlwHistoryItemMapper;
import com.nbicc.gywlw.mapper.GywlwUserMapper;
import com.nbicc.gywlw.util.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ManufacturerService {
	
	@Autowired
	private GywlwUserMapper gywlwUserMapper;
	
	@Autowired
	private GywlwDeviceMapper gywlwDeviceMapper;
	
	@Autowired
	private GywlwHistoryItemMapper gywlwHistoryItemMapper;

	@Autowired
	private ProjectService projectService;
	
	
	public List<GywlwUser> searchUser(String factoryId,int level,String name){
		return gywlwUserMapper.searchUserByFactoy(factoryId,level,name);
	}
	
	public List<GywlwDevice> searchDevice(String adminId){
		return gywlwDeviceMapper.searchDeviceByFactory(adminId);
	}
	
	public List<GywlwHistoryItem> getHistoryData(String deviceId){
		projectService.refreshData();
		return gywlwHistoryItemMapper.getHistoryData(deviceId);
	}
	
	public List<GywlwHistoryItem> getDeviceAlarmlist(String startTime,String endTime,String deviceId){
		projectService.refreshData();
		return gywlwHistoryItemMapper.getDeviceAlarmlist(MyUtil.timeTransformToString(startTime),
				MyUtil.timeTransformToString(endTime), deviceId);
	}
	
	public GywlwHistoryItem getAlarmDetail(String itemId){
		projectService.refreshData();
		return gywlwHistoryItemMapper.getAlarmDetail(itemId);
	}
	
	public List<GywlwUser> getFactoryLimitUser(String name,String id){
		return gywlwUserMapper.getFactoryLimitUser(name, id);
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
		return gywlwDeviceMapper.getFactoryDevicelist(factoryId, deviceSn,level);
	}

}
