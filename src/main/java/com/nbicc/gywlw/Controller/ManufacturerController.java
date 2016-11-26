package com.nbicc.gywlw.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nbicc.gywlw.Model.GywlwDevice;
import com.nbicc.gywlw.Model.GywlwUser;
import com.nbicc.gywlw.Model.HostHolder;
import com.nbicc.gywlw.Service.ManufacturerService;
import com.nbicc.gywlw.util.MyUtil;
import com.nbicc.gywlw.util.ResponseCode;

import net.sf.json.JSONObject;

@Controller
public class ManufacturerController {

	 private static final Logger logger = LoggerFactory.getLogger(ManufacturerController.class);
	
	@Autowired
	private HostHolder hostHolder;
	
	@Autowired
	private ManufacturerService manufacturerService;

	@RequestMapping(path = { "/factory/userlist" }, method = { RequestMethod.POST })
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
	
	
	@RequestMapping(path = { "/factory/devicelist" }, method = { RequestMethod.POST })
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
	
	@RequestMapping(path = { "/factory/deviceDatalist" }, method = { RequestMethod.POST })
	@ResponseBody
	public JSONObject deviceDatalist(@RequestParam(value = "adminId") String adminId) {
		try{
            List<GywlwDevice> list = manufacturerService.searchDevice(adminId);
            return ResponseCode.response(0, list);
        }catch (Exception e){
            logger.error("搜索设备失败" + e.getMessage());
            return ResponseCode.response(1, "搜索设备失败");
        }
	}
	
	
}
