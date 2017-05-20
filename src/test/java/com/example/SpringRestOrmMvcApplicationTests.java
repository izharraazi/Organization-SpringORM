package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lab2.SpringRestOrmMvcApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringRestOrmMvcApplication.class)
@WebAppConfiguration
public class SpringRestOrmMvcApplicationTests {

	@Test
	public void contextLoads() {
	}

}
