package com.practice.project01;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class HelloworldTests {

	@Autowired
	private MockMvc mvc;

	@Test
	public void helloworld() throws Exception{
		mvc.perform(MockMvcRequestBuilders.get("/api/v1/hello-world")).andDo(
				MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
						.andExpect(MockMvcResultMatchers.jsonPath("$.Name", Matchers.is("Ahmad"))

//						.andExpect(MockMvcResultMatchers.jsonPath("$.SendingMessage", "Yes")
		);
	}

	@Test
	public void randomAPI() throws Exception{
		mvc.perform(MockMvcRequestBuilders.get("/api/v1/date-time"))
				.andDo(
						MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.Sending", Matchers.notNullValue())



				);



	}

}
