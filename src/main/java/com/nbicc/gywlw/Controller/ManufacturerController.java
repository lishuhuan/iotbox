package com.nbicc.gywlw.Controller;

import com.alibaba.fastjson.JSONObject;
import com.nbicc.gywlw.Model.GywlwDevice;
import com.nbicc.gywlw.Model.GywlwHistoryItem;
import com.nbicc.gywlw.Model.GywlwUser;
import com.nbicc.gywlw.Model.HostHolder;
import com.nbicc.gywlw.Service.ManufacturerService;
import com.nbicc.gywlw.Service.MessageService;
import com.nbicc.gywlw.util.MyUtil;
import com.nbicc.gywlw.util.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/factory")
public class ManufacturerController {

	 private static final Logger logger = LoggerFactory.getLogger(ManufacturerController.class);

	@Autowired
	private MessageService messageService;
	@Autowired
	private HostHolder hostHolder;
	
	@Autowired
	private ManufacturerService manufacturerService;

	@RequestMapping(path = { "/userlist" }, method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public JSONObject userlist(@RequestParam(value = "name") String name) {
		try{
			String id=hostHolder.getGywlwUser().getUserId();
			int level=hostHolder.getGywlwUser().getDuserLevel();
            List<GywlwUser> list = manufacturerService.searchUser(id,level,name);
            return ResponseCode.response(0, list);
        }catch (Exception e){
            logger.error("搜索用户失败" + e.getMessage());
            return ResponseCode.response(1, "搜索用户失败");
        }
	}
	
	
	@RequestMapping(path = { "/devicelist" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject devicelist(@RequestParam(value = "adminId") String adminId) {
		try{
            List<GywlwDevice> list = manufacturerService.searchDevice(adminId);
            return ResponseCode.response(0, list);
        }catch (Exception e){
            logger.error("搜索设备失败" + e.getMessage());
            return ResponseCode.response(1, "搜索设备失败");
        }
	}
	//设备实时数据
	@RequestMapping(path = { "/deviceDatalist" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject deviceDatalist(@RequestParam(value = "deviceId") String deviceId) {
		try{

            List<GywlwHistoryItem> list = manufacturerService.getHistoryData(deviceId);
            return ResponseCode.response(0, list);
        }catch (Exception e){
            logger.error("获取数据失败" + e.getMessage());
            return ResponseCode.response(1, "获取数据失败");
        }
	}

	//设备实时告警
	@RequestMapping(path = { "/deviceAlarmlist" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject deviceAlarmlist(@RequestParam(value = "startTime",defaultValue = "0") String startTime,
									  @RequestParam(value = "endTime",defaultValue = "2480036920") String endTime,
									  @RequestParam(value = "deviceId") String deviceId) {
		try{
            List<GywlwHistoryItem> list = manufacturerService.getDeviceAlarmlist(startTime,endTime,deviceId);
            return ResponseCode.response(0, list);
        }catch (Exception e){
            logger.error("获取告警失败" + e.getMessage());
            return ResponseCode.response(1, "获取告警失败");
        }
	}
	
	@RequestMapping(path = { "/alarmDetail" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject alarmDetail(@RequestParam(value = "itemId") String deviceId) {
		try{
            GywlwHistoryItem item= manufacturerService.getAlarmDetail(deviceId);
            return ResponseCode.response(0, item);
        }catch (Exception e){
            logger.error("获取告警详情失败" + e.getMessage());
            return ResponseCode.response(1, "获取告警详情失败");
        }
	}
	
	@RequestMapping(path = { "/factoryLimitUser" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject factoryLimitUser(@RequestParam(value = "name") String name) {
		String id=hostHolder.getGywlwUser().getUserId();
		try{
           List<GywlwUser> list=manufacturerService.getFactoryLimitUser(name,id);
            return ResponseCode.response(0, list);
        }catch (Exception e){
            logger.error("获取厂商受限用户失败" + e.getMessage());
            return ResponseCode.response(1, "获取厂商受限用户失败");
        }
	}
	
	@RequestMapping(path = { "/factoryLimitUserDistribution" }, method = { RequestMethod.POST })
	@ResponseBody
	public String factoryLimitUserDistribution(@RequestParam(value = "factoryId") String factoryId,
												   @RequestParam(value = "userId") String userId,
												   @RequestParam(value = "tag") String tag){
		try {
			if("0".equals(tag)) {
				messageService.sendMessage(hostHolder.getGywlwUser().getUserId(), factoryId, "管理员向你分配客户",
						Byte.parseByte("3"), userId);
			}else if("1".equals(tag)){
				boolean state=manufacturerService.editFactoryLimitUserDistribution(factoryId,
						userId, Integer.parseInt("1"));
			}
			return MyUtil.getJSONString(0,"发送邀请成功");
		}catch (Exception e){
			logger.error("发送失败" + e.getMessage());
			return MyUtil.getJSONString(1, "发送失败!");
		}
	}
	
	@RequestMapping(path = { "/factoryDevicelist" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject factoryDevicelist(@RequestParam(value = "factoryId") String factoryId,
										@RequestParam(value = "deviceSn") String deviceSn) {
		int level=hostHolder.getGywlwUser().getDuserLevel();
		try{
            List<GywlwDevice> list = manufacturerService.getFactoryDevicelist(factoryId,deviceSn,level);
            return ResponseCode.response(0, list);
        }catch (Exception e){
            logger.error("搜索设备失败" + e.getMessage());
            return ResponseCode.response(1, "搜索设备失败");
        }
	}
	
	
}
