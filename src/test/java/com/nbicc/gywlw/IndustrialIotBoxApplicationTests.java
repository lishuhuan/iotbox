package com.nbicc.gywlw;

import com.nbicc.gywlw.Model.GywlwProject;
import com.nbicc.gywlw.Service.ProjectService;
import com.nbicc.gywlw.Service.UserService;
import com.taobao.api.ApiException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author   fangdong
 * @create   2016.12.8
 */


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IndustrialIotBoxApplication.class)
@WebAppConfiguration
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class IndustrialIotBoxApplicationTests {

//	@Test
//	public void contextLoads() {
//	}

	@Autowired
	private UserService userService;
	@Resource
	private ProjectService projectService;

	@Test
	public void RegisterTest() throws ApiException {
		String smsCode = userService.sendSms("13000001112");
		Map<String,Object> map = userService.register("测试233","123","13000001112","company2",smsCode);
		assertTrue("have ticket? ",map.containsKey("ticket"));
	}

	@Test
	public void ProjectListTest(){
		List<GywlwProject> list = projectService.projectList("1",Byte.parseByte("0"));
		assertNotNull(list);
	}




}
