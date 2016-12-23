package com.nbicc.gywlw.Controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nbicc.gywlw.Model.*;
import com.nbicc.gywlw.Service.ManufacturerService;
import com.nbicc.gywlw.Service.MessageService;
import com.nbicc.gywlw.util.MyUtil;
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
            return MyUtil.response(0, list);
        }catch (Exception e){
            logger.error("搜索用户失败" + e.getMessage());
            return MyUtil.response(1, "搜索用户失败");
        }
	}

	//by page
	@RequestMapping(path = { "/userlistbypage" }, method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public JSONObject userlistByPage(@RequestParam(value = "name") String name,
									 @RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
									 @RequestParam(value = "page_size", defaultValue = "6")Integer pageSize) {
		try{
			String id=hostHolder.getGywlwUser().getUserId();
			int level=hostHolder.getGywlwUser().getDuserLevel();
			PageHelper.startPage(pageNum,pageSize);
			List<GywlwUser> list = manufacturerService.searchUser(id,level,name);
			PageInfo<GywlwUser> pageInfo = new PageInfo<>(list);
			return MyUtil.response(0, pageInfo);
		}catch (Exception e){
			logger.error("搜索用户失败" + e.getMessage());
			return MyUtil.response(1, "搜索用户失败");
		}
	}
	
	
	@RequestMapping(path = { "/devicelist" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject devicelist(@RequestParam(value = "admin_id") String adminId) {
		try{
            List<GywlwDevice> list = manufacturerService.searchDevice(adminId);
            return MyUtil.response(0, list);
        }catch (Exception e){
            logger.error("搜索设备失败" + e.getMessage());
            return MyUtil.response(1, "搜索设备失败");
        }
	}

	//by page
	@RequestMapping(path = { "/devicelistbypage" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject devicelist(@RequestParam(value = "admin_id") String adminId,
								 @RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
								 @RequestParam(value = "page_size", defaultValue = "6")Integer pageSize) {
		try{
			PageHelper.startPage(pageNum,pageSize);
			List<GywlwDevice> list = manufacturerService.searchDevice(adminId);
			PageInfo<GywlwDevice> pageInfo = new PageInfo<>(list);
			return MyUtil.response(0, pageInfo);
		}catch (Exception e){
			logger.error("搜索设备失败" + e.getMessage());
			return MyUtil.response(1, "搜索设备失败");
		}
	}

	//设备实时数据
	@RequestMapping(path = { "/devicedatalist" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject deviceDatalist(@RequestParam(value = "device_id") String deviceId) {
		try{
            List<GywlwHistoryItem> list = manufacturerService.getHistoryData(deviceId);
            return MyUtil.response(0, list);
        }catch (Exception e){
            logger.error("获取数据失败" + e.getMessage());
            return MyUtil.response(1, "获取数据失败");
        }
	}

	//设备实时数据 by page
	@RequestMapping(path = { "/devicedatalistbypage" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject deviceDatalistByPage(@RequestParam(value = "device_id") String deviceId,
										   @RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
										   @RequestParam(value = "page_size", defaultValue = "6")Integer pageSize) {
		try{
			PageHelper.startPage(pageNum,pageSize);
			List<GywlwHistoryItem> list = manufacturerService.getHistoryData(deviceId);
			PageInfo<GywlwHistoryItem> pageInfo = new PageInfo<>(list);
			return MyUtil.response(0, pageInfo);
		}catch (Exception e){
			logger.error("获取数据失败" + e.getMessage());
			return MyUtil.response(1, "获取数据失败");
		}
	}

	//设备实时告警
	@RequestMapping(path = { "/devicealarmlist" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject deviceAlarmlist(@RequestParam(value = "start_time",defaultValue = "0") String startTime,
									  @RequestParam(value = "end_time",defaultValue = "2480036920") String endTime,
									  @RequestParam(value = "device_id") String deviceId) {
		try{
            List<GywlwHistoryItem> list = manufacturerService.getDeviceAlarmlist(startTime,endTime,deviceId);
            return MyUtil.response(0, list);
        }catch (Exception e){
            logger.error("获取告警失败" + e.getMessage());
            return MyUtil.response(1, "获取告警失败");
        }
	}

	//设备实时告警 by page
	@RequestMapping(path = { "/devicealarmlistbypage" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject deviceAlarmlistByPage(@RequestParam(value = "start_time",defaultValue = "0") String startTime,
									 		@RequestParam(value = "end_time",defaultValue = "2480036920") String endTime,
									  		@RequestParam(value = "device_id") String deviceId,
											@RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
											@RequestParam(value = "page_size", defaultValue = "6")Integer pageSize		) {
		try{
			PageHelper.startPage(pageNum,pageSize);
			List<GywlwHistoryItem> list = manufacturerService.getDeviceAlarmlist(startTime,endTime,deviceId);
			PageInfo<GywlwHistoryItem> pageInfo = new PageInfo<>(list);
			return MyUtil.response(0, pageInfo);
		}catch (Exception e){
			logger.error("获取告警失败" + e.getMessage());
			return MyUtil.response(1, "获取告警失败");
		}
	}
	
	@RequestMapping(path = { "/alarmdetail" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject alarmDetail(@RequestParam(value = "item_id") String deviceId) {
		try{
            GywlwHistoryItem item= manufacturerService.getAlarmDetail(deviceId);
            return MyUtil.response(0, item);
        }catch (Exception e){
            logger.error("获取告警详情失败" + e.getMessage());
            return MyUtil.response(1, "获取告警详情失败");
        }
	}
	
	@RequestMapping(path = { "/factorylimituser" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject factoryLimitUser(@RequestParam(value = "user_id") String userId) {
		String id=hostHolder.getGywlwUser().getUserId();
		try{
           List<GywlwUser> list=manufacturerService.getFactoryLimitUser(userId,id);
            return MyUtil.response(0, list);
        }catch (Exception e){
            logger.error("获取厂商受限用户失败" + e.getMessage());
            return MyUtil.response(1, "获取厂商受限用户失败");
        }
	}

	//by page
	@RequestMapping(path = { "/factorylimituserbypage" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject factoryLimitUserByPage(@RequestParam(value = "user_id") String userId,
											 @RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
											 @RequestParam(value = "page_size", defaultValue = "6")Integer pageSize) {
		String id=hostHolder.getGywlwUser().getUserId();
		try{
			PageHelper.startPage(pageNum,pageSize);
			List<GywlwUser> list=manufacturerService.getFactoryLimitUser(userId,id);
			PageInfo<GywlwUser> pageInfo = new PageInfo<>(list);
			return MyUtil.response(0, pageInfo);
		}catch (Exception e){
			logger.error("获取厂商受限用户失败" + e.getMessage());
			return MyUtil.response(1, "获取厂商受限用户失败");
		}
	}
	
	@RequestMapping(path = { "/factoryLimituserdistribution" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject factoryLimitUserDistribution(@RequestParam(value = "factory_id") String factoryId,
												   @RequestParam(value = "user_id") String userId,
												   @RequestParam(value = "tag") String tag){
		try {
			if("0".equals(tag)) {
				messageService.sendMessage(hostHolder.getGywlwUser().getUserId(), factoryId, "管理员向你分配客户",
						Byte.parseByte("3"), userId);
			}else if("1".equals(tag)){
				boolean state=manufacturerService.editFactoryLimitUserDistribution(factoryId,
						userId, Integer.parseInt("1"));
			}
			return MyUtil.response(0,"发送邀请成功");
		}catch (Exception e){
			logger.error("发送失败" + e.getMessage());
			return MyUtil.response(1, "发送失败!");
		}
	}
	
	@RequestMapping(path = { "/factorydevicelist" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject factoryDevicelist(@RequestParam(value = "device_sn") String deviceSn) {
		int level=hostHolder.getGywlwUser().getDuserLevel();
		try{
            List<GywlwDevice> list = manufacturerService.getFactoryDevicelist(hostHolder.getGywlwUser().getUserId(),
					deviceSn,level);
            return MyUtil.response(0, list);
        }catch (Exception e){
            logger.error("搜索设备失败" + e.getMessage());
            return MyUtil.response(1, "搜索设备失败");
        }
	}

	//by page
	@RequestMapping(path = { "/factorydevicelistbypage" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject factoryDevicelistByPage(@RequestParam(value = "device_sn") String deviceSn,
											  @RequestParam(value = "page_num", defaultValue = "1")Integer pageNum,
											  @RequestParam(value = "page_size", defaultValue = "6")Integer pageSize ) {
		int level=hostHolder.getGywlwUser().getDuserLevel();
		try{
			PageHelper.startPage(pageNum,pageSize);
			List<GywlwDevice> list = manufacturerService.getFactoryDevicelist(hostHolder.getGywlwUser().getUserId(),deviceSn,level);
			PageInfo<GywlwDevice> pageInfo = new PageInfo<>(list);
			return MyUtil.response(0, pageInfo);
		}catch (Exception e){
			logger.error("搜索设备失败" + e.getMessage());
			return MyUtil.response(1, "搜索设备失败");
		}
	}
	
	
}
