package com.nbicc.gywlw.util;

import com.alibaba.fastjson.JSONObject;

public class ResponseCode {

	
	public static JSONObject response(int code,Object object){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("result_code", code);
		jsonObject.put("data", object);
		return jsonObject;
	}
}
