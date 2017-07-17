package com.srikanth.springboot.ui;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
@WebAppConfiguration
public class SpringBootDatatableApplicationTests {
	

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.build();
	}
	
	@Test
	public void contextLoads() {
		//mvc.perform(get("/"));
	}

}
