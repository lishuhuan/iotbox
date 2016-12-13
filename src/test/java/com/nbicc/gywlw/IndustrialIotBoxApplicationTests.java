package com.nbicc.gywlw;

import com.nbicc.gywlw.Service.UserService;
import com.taobao.api.ApiException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author   fangdong
 * @create   2016.12.8
 */


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IndustrialIotBoxApplication.class)
@WebAppConfiguration
public class IndustrialIotBoxApplicationTests {

//	@Test
//	public void contextLoads() {
//	}

	@Autowired
	private UserService userService;

	@Test
	public void RegisterTest() throws ApiException {
		String smsCode = userService.sendSms("13000001111");
		Map<String,Object> map = userService.register("测试233","123","13000001111","company2",smsCode);
		assertTrue("have ticket? ",map.containsKey("ticket"));

	}




}
