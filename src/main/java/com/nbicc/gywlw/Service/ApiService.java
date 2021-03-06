package com.nbicc.gywlw.Service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nbicc.gywlw.Dao.LoginTicketDAO;
import com.nbicc.gywlw.Model.GywlwDevice;
import com.nbicc.gywlw.Model.GywlwDeviceOrder;
import com.nbicc.gywlw.Model.GywlwHistoryItem;
import com.nbicc.gywlw.Model.GywlwPlcInfo;
import com.nbicc.gywlw.Model.GywlwRegInfo;
import com.nbicc.gywlw.Model.GywlwUser;
import com.nbicc.gywlw.Model.HostHolder;
import com.nbicc.gywlw.Model.LoginTicket;
import com.nbicc.gywlw.mapper.GywlwDataTrendMapper;
import com.nbicc.gywlw.mapper.GywlwDeviceMapper;
import com.nbicc.gywlw.mapper.GywlwDeviceOrderMapper;
import com.nbicc.gywlw.mapper.GywlwHistoryItemMapper;
import com.nbicc.gywlw.mapper.GywlwMessageMapper;
import com.nbicc.gywlw.mapper.GywlwPlcInfoMapper;
import com.nbicc.gywlw.mapper.GywlwProjectDeviceGroupMapper;
import com.nbicc.gywlw.mapper.GywlwProjectMapper;
import com.nbicc.gywlw.mapper.GywlwProjectUserGroupMapper;
import com.nbicc.gywlw.mapper.GywlwRegInfoMapper;
import com.nbicc.gywlw.mapper.GywlwUserMapper;
import com.nbicc.gywlw.mapper.GywlwVariableMapper;
import com.nbicc.gywlw.mapper.GywlwVariableRegGroupMapper;
import com.nbicc.gywlw.mapper.GywlwWarningRulesMapper;
import com.nbicc.gywlw.util.HttpClientUtil;
import com.nbicc.gywlw.util.HttpsTest;
import com.nbicc.gywlw.util.MyUtil;

/**
 * Created by BigMao on 2017/1/18. 对应BaseAPI
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

	public JSONObject deviceInfo(String token) {
		GywlwUser gywlwUser;
		if (token != null) {
			// 解析token
			LoginTicket loginTicket = loginTicketDAO.selectByTicket(token);
			if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
				return MyUtil.response(1, "请重新登录");
			}
			gywlwUser = gywlwUserMapper.selectByPrimaryKey(loginTicket.getUserId());
			// 验证设备是否是该用户所有
			List<GywlwDevice> gywlwDevices = gywlwDeviceMapper.selectByAdminId(gywlwUser.getUserId());
			if (gywlwDevices.size() == 0) {
				return MyUtil.response(2, "您未绑定任何工业物联网盒子");
			}
			List<GywlwDevice> newList = new ArrayList<>();
			for (GywlwDevice gywlwDevice : gywlwDevices) {
				GywlwDevice device = new GywlwDevice();
				device.setDeviceName(gywlwDevice.getDeviceName());
				device.setHardwareEdition(gywlwDevice.getHardwareEdition());
				device.setSoftwareEdition(gywlwDevice.getSoftwareEdition());
				device.setDeviceSn(gywlwDevice.getDeviceSn());
				device.setThreeGMode(gywlwDevice.getThreeGMode());
				newList.add(device);
			}
			return MyUtil.response(0, newList);
		}
		return MyUtil.response(1, "请重新登录");
	}

	public JSONObject deviceStatus(String token, String deviceId) {
		GywlwUser gywlwUser;
		if (token != null) {
			// 解析token
			LoginTicket loginTicket = loginTicketDAO.selectByTicket(token);
			if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
				return MyUtil.response(1, "请重新登录");
			}
			gywlwUser = gywlwUserMapper.selectByPrimaryKey(loginTicket.getUserId());
			// 验证设备是否是该用户所有
			List<GywlwDevice> gywlwDevices = gywlwDeviceMapper.selectByAdminId(gywlwUser.getUserId());
			if (gywlwDevices.size() == 0) {
				return MyUtil.response(2, "您未绑定任何工业物联网盒子");
			}
			List<GywlwDevice> newList = new ArrayList<>();
			for (GywlwDevice gywlwDevice : gywlwDevices) {
				GywlwDevice device = new GywlwDevice();
				device.setDeviceStatus(gywlwDevice.getDeviceStatus());
				device.setDeviceName(gywlwDevice.getDeviceName());
				if ("3".equals(gywlwDevice.getDeviceStatus())) {
					GywlwDeviceOrder gywlwDeviceOrder = gywlwDeviceOrderMapper
							.selectByDeviceId(gywlwDevice.getDeviceId());
					device.setOrderCode(gywlwDeviceOrder.getOrderCode());
					device.setOrderTotal(gywlwDeviceOrder.getOrderTotal());
				}
				newList.add(device);
			}
			return MyUtil.response(0, newList);
		}
		return MyUtil.response(1, "请重新登录");
	}

	@Transactional
	public JSONObject uploadOrderInfo(String token, String deviceId, String orderCode, String orderNum) {
		JSONObject result = checkAdminAndDevice(token, deviceId);
		if (result != null) {
			return result;
		}
		GywlwDeviceOrder gywlwDeviceOrder = gywlwDeviceOrderMapper.selectByDeviceId(deviceId);
		if (gywlwDeviceOrder == null) {
			GywlwDeviceOrder order = new GywlwDeviceOrder();
			order.setDeviceId(deviceId);
			order.setDeviceSn(gywlwDeviceMapper.selectByDeviceId(deviceId).getDeviceSn());
			order.setOrderTotal(Integer.parseInt(orderNum));
			order.setOrderCode(orderCode);
			order.setCreatedTime(new Date());
			gywlwDeviceOrderMapper.insertSelective(order);
		} else {
			gywlwDeviceOrder.setOrderCode(orderCode);
			gywlwDeviceOrder.setOrderTotal(Integer.parseInt(orderNum));
			gywlwDeviceOrderMapper.updateByPrimaryKeySelective(gywlwDeviceOrder);
		}

		GywlwDevice device1 = new GywlwDevice();
		device1.setDeviceId(deviceId);
		device1.setDeviceStatus("3");
		gywlwDeviceMapper.updateByPrimaryKeySelective(device1);
		return MyUtil.response(0, "上传订单信息成功！");
	}

	public JSONObject regInfo(String token, String deviceId) {
		JSONObject result = checkAdminAndDevice(token, deviceId);
		if (result != null) {
			return result;
		}
		List<GywlwRegInfo> list = new ArrayList<>();
		// gywlwRegInfoMapper.selectByPlcId(gywlwPlcInfoMapper.selectByDeviceId1(deviceId).getId());
		List<GywlwPlcInfo> plcInfoList = gywlwPlcInfoMapper.selectByDeviceId(deviceId);
		for (GywlwPlcInfo plcInfo : plcInfoList) {
			if (gywlwRegInfoMapper.selectByPlcId(plcInfo.getId()) != null) {
				list.addAll(gywlwRegInfoMapper.selectByPlcId(plcInfo.getId()));
			}
		}
		for (GywlwRegInfo reginfo : list) {
			GywlwHistoryItem item = gywlwHistoryItemMapper.getDataForRegId(reginfo.getRegId());
			if (item == null) {
				continue;
			}
			reginfo.setValue(item.getItemValue());
			reginfo.setLastConnect(item.getItemTime());
		}
		return MyUtil.response(0, list);
	}

	public JSONObject getRules(String token, String deviceId) {
		JSONObject result = checkAdminAndDevice(token, deviceId);
		if (result == null) {
			return MyUtil.response(0, gywlwWarningRulesMapper.selectByDeviceId(deviceId));
		}
		return result;
	}

	public JSONObject checkAdminAndDevice(String token, String deviceId) {
		if (token != null) {
			// 解析token
			LoginTicket loginTicket = loginTicketDAO.selectByTicket(token);
			if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
				return MyUtil.response(1, "请重新登录");
			}
			GywlwUser gywlwUser = gywlwUserMapper.selectByPrimaryKey(loginTicket.getUserId());
			hostHolder.setGywlwUser(gywlwUser);
			GywlwDevice gywlwDevice = gywlwDeviceMapper.selectByDeviceId(deviceId);
			if (gywlwDevice == null) {
				return MyUtil.response(1, "找不到该设备");
			} else {
				if (gywlwUser.getUserId().equals(gywlwDevice.getAdminId())) {
					return null;
				} else {
					return MyUtil.response(1, "无权访问该设备");
				}
			}

		}
		return MyUtil.response(1, "请重新登录");

	}

	public JSONObject plcInfo(String token, String deviceId) {
		JSONObject result = checkAdminAndDevice(token, deviceId);
		if (result == null) {
			// 只提供必要的信息，其他的不显示
			List<GywlwPlcInfo> list1 = new ArrayList<>();
			List<GywlwPlcInfo> list = gywlwPlcInfoMapper.selectByDeviceId(deviceId);
			if (list.size() > 0) {
				for (GywlwPlcInfo plcinfo : list) {
					GywlwPlcInfo plc = new GywlwPlcInfo();
					plc.setPlcName(plcinfo.getPlcName());
					plc.setPlcType(plcinfo.getPlcType());
					plc.setDeviceId(plcinfo.getDeviceId());
					plc.setPlcBrand(plcinfo.getPlcBrand());
					list1.add(plc);
				}
			}
			return MyUtil.response(0, list1);
		}
		return result;
	}

	@Transactional
	public void testTransaction() {
		GywlwPlcInfo info = new GywlwPlcInfo();
		info.setId("233");
		gywlwPlcInfoMapper.insertSelective(info);
		int y = Integer.parseInt("a");
	}

	public JSONObject getDeviceTotalOrder(String deviceId, String token) {
		// TODO Auto-generated method stub
		LoginTicket loginTicket = loginTicketDAO.selectByTicket(token);
		if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
			return MyUtil.response(1, "请重新登录");
		}
		if (deviceId == null || "".equals(deviceId)) {
			GywlwUser gywlwUser = gywlwUserMapper.selectByPrimaryKey(loginTicket.getUserId());
			List<GywlwDevice> gywlwDevices = gywlwDeviceMapper.selectTotalAndDeviceByAdminId(gywlwUser.getUserId());

			JSONArray jsonArray = new JSONArray();
			for (GywlwDevice device : gywlwDevices) {
				String id = device.getGpioId();
				if (id != null && !"".equals(id)) {
					JSONObject jsonObject = setData(id);
					jsonArray.add(jsonObject);
				}
			}
			return MyUtil.response(0, jsonArray);
		} else {
			JSONObject jsonObject = setData(deviceId);
			return MyUtil.response(0, jsonObject);
		}
	}

	private JSONObject setData(String deviceId) {
		// TODO Auto-generated method stubint
		// total=gywlwDeviceOrderMapper.getDeviceTotalOrder(deviceId);
		JSONObject data = getDataFromPlatform(deviceId, "?id=throughput,gpio_6,3g_mode,time");
		JSONObject jsonObject = new JSONObject();
		if (data.containsKey("3g_mode")) {
			String mode = data.getString("3g_mode");
			jsonObject.put("threeGMode", mode);
		}
		if (data.containsKey("gpio_6")) {
			String gpio_6 = data.getJSONObject("gpio_6").getString("value");
			jsonObject.put("status", gpio_6);
		}
		if (data.containsKey("time")) {
			String time = data.getString("time");
			jsonObject.put("time", time);
		}
		if (data.containsKey("throughput")) {
			String throughput = data.getJSONObject("throughput").getString("value");
			jsonObject.put("total", throughput);
		}
		jsonObject.put("device_id", deviceId);
		jsonObject.put("device_name", "");
		return jsonObject;
	}

	private JSONObject getDataFromPlatform(String deviceId, String params) {
		try {
			String tokenJson = HttpClientUtil.getMethod(
					"https://iot-expeed.tech:8083/platform/getToken?platform_key=85805d7c2fae49cc8b7230a430028088&secret_key=2069bfe7b38b4599b17f2a2513c127f8");
			JSONObject jsonObject = JSONObject.parseObject(tokenJson);
			int resultCode = jsonObject.getIntValue("result_code");
			if (resultCode == 0) {
				JSONObject data = jsonObject.getJSONObject("data");
				String token = data.getString("token");
				String url = "https://iot-expeed.tech:8083/device/" + deviceId + "/status" + params;
				String result = HttpClientUtil.getMethodWithHeader(url, token);
				JSONObject reJson = JSONObject.parseObject(result);
				int code = reJson.getIntValue("result_code");
				if (code == 0) {
					JSONObject datajson = reJson.getJSONObject("data");
					return datajson;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} catch (KeyManagementException | NoSuchAlgorithmException | ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public JSONObject getDeviceAlarm(String deviceId, long startTime, long endTime, String alarmLevel,
			String token) {
		// TODO Auto-generated method stub
		LoginTicket loginTicket = loginTicketDAO.selectByTicket(token);
		if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
			return MyUtil.response(1, "请重新登录");
		}
		if (deviceId == null || "".equals(deviceId)) {
			GywlwUser gywlwUser = gywlwUserMapper.selectByPrimaryKey(loginTicket.getUserId());
			List<GywlwDevice> gywlwDevices = gywlwDeviceMapper.selectTotalAndDeviceByAdminId(gywlwUser.getUserId());

			JSONArray jsonArray = new JSONArray();
			int tag = 1;
			for (GywlwDevice device : gywlwDevices) {
				String id = device.getGpioId();
				if (id != null && !"".equals(id)) {
					JSONArray array = getHistoryDataFromPlatform(id, startTime);
					if (array != null) {
						for (int i = 0; i < array.size(); i++) {
							JSONObject object = array.getJSONObject(i);
							JSONObject json52 = setAlarmData52(object, endTime, alarmLevel, id, tag);
							JSONObject json53 = setAlarmData53(object, endTime, alarmLevel, id, tag + 1);
							if(json52!=null){
								jsonArray.add(json52);
								tag++;
							}
							if(json53!=null){
								jsonArray.add(json53);
								tag++;
							}
						}
					}

				}
			}
			return MyUtil.response(0, jsonArray);
		} else {
			JSONArray jsonArray = new JSONArray();
			JSONArray array = getHistoryDataFromPlatform(deviceId, startTime);
			if (array != null) {
				int tag = 1;
				for (int i = 0; i < array.size(); i++) {
					JSONObject object = array.getJSONObject(i);
					JSONObject json52 = setAlarmData52(object, endTime, alarmLevel, deviceId, tag);
					JSONObject json53 = setAlarmData53(object, endTime, alarmLevel, deviceId, tag + 1);
				
					if (json52 != null) {
						jsonArray.add(json52);
						tag++;
					}
					if (json53 != null) {
						jsonArray.add(json53);
						tag++;
					}

				}
			}
			return MyUtil.response(0, jsonArray);
		}
	}

	private JSONArray getHistoryDataFromPlatform(String id, long startTime) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("deviceId", id);
		List<String> list = new ArrayList<>();
		list.add("52");
		list.add("53");
		jsonObject.put("idList", list);
		jsonObject.put("timestamp", startTime);
		String result = null;
		try {
			result = HttpsTest.postForm(jsonObject, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject object = JSONObject.parseObject(result);
		int result_code = object.getIntValue("result_code");
		if (result_code == 0) {
			return object.getJSONArray("result_data");
		} else {
			return null;
		}
	}

	private JSONObject setAlarmData52(JSONObject data, long endTime, String level, String deviceId,
			int serial) {
		long time = data.getLongValue("timestamp");
		if (time < endTime) {
			JSONObject json52 = data.getJSONObject("52");
			if (json52 != null) {
				JSONObject rejson = trasfer(json52, serial, deviceId, 1, time, level);
				if (rejson != null) {
					return rejson;
				} else {
					return null;
				}

			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private JSONObject setAlarmData53(JSONObject data, long endTime, String level, String deviceId,
			int serial) {
		long time = data.getLongValue("timestamp");
		if (time < endTime) {
			JSONObject json53 = data.getJSONObject("53");
			if (json53 != null) {
				JSONObject rejson = trasfer(json53, serial, deviceId, 0, time, level);
				if (rejson != null) {
					return rejson;
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private JSONObject trasfer(JSONObject json52, int i, String deviceId, int value, long time, String level) {
		// TODO Auto-generated method stub
		if ("".equals(level) || json52.getString("rules_alarmlevel").equals(level)) {
			JSONObject object = new JSONObject();
			object.put("id", i);
			object.put("device_id", deviceId);
			object.put("alarm_id", json52.getString("rules_id"));
			object.put("alarm_name", json52.getString("rules_name"));
			object.put("alarm_name2", json52.getString("rules_name2"));
			object.put("alarm_level", json52.getString("rules_alarmlevel"));
			object.put("time", time/1000);
			object.put("rule_field", json52.getString("rules_field"));
			object.put("rule_condition", json52.getString("rules_condition"));
			object.put("gpio_id", "");
			object.put("value", value);
			return object;
		} else {
			return null;
		}
	}

}
