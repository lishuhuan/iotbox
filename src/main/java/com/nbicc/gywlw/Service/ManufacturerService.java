package com.nbicc.gywlw.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nbicc.gywlw.Model.GywlwDevice;
import com.nbicc.gywlw.Model.GywlwHistoryItem;
import com.nbicc.gywlw.Model.GywlwUser;
import com.nbicc.gywlw.mapper.GywlwDeviceMapper;
import com.nbicc.gywlw.mapper.GywlwHistoryItemMapper;
import com.nbicc.gywlw.mapper.GywlwManufacturerMapper;
import com.nbicc.gywlw.mapper.GywlwUserMapper;

@Service
public class ManufacturerService {
	
	@Autowired
	private GywlwUserMapper gywlwUserMapper;
	
	@Autowired
	private GywlwDeviceMapper gywlwDeviceMapper;
	
	@Autowired
	private GywlwHistoryItemMapper gywlwHistoryItemMapper;
	
	
	public List<GywlwUser> searchUser(String factoryId,int level,String name){
		return gywlwUserMapper.searchUserByFactoy(factoryId,level,name);
	}
	
	public List<GywlwDevice> searchDevice(String adminId){
		return gywlwDeviceMapper.searchDeviceByFactory(adminId);
	}
	
	public List<GywlwHistoryItem> getHistoryData(String deviceId){
		return gywlwHistoryItemMapper.getHistoryData(deviceId);
	}
	
	public List<GywlwHistoryItem> getDeviceAlarmlist(String startTime,String endTime,String deviceId){
		return gywlwHistoryItemMapper.getDeviceAlarmlist(startTime, endTime, deviceId);
	}
	
	public GywlwHistoryItem getAlarmDetail(String itemId){
		return gywlwHistoryItemMapper.getAlarmDetail(itemId);
	}

}
