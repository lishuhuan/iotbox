package com.nbicc.gywlw;

import com.nbicc.gywlw.Service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.junit.Assert.assertArrayEquals;
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
	public void UserServiceTest(){
		assertArrayEquals(
				new Object[]{

				},
				new Object[]{

				}

		);
	}




}
