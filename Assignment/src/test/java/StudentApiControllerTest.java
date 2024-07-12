
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project06.Project05Application;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


@AutoConfigureMockMvc
@SpringBootTest(classes = Project05Application.class)
public class StudentApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testStudentSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Hello World!")))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.at", Matchers.notNullValue()));
    }

    @Test
    public void testStudentsGetNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student/0"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Order(1)
    @Test
    public void testStudentGetListSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }

    @Order(2)
    @Test
    public void testPostStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/student")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"Ali\",\"roll_num\":\"bsef20\", \"university\":\"pucit\", \"cgpa\":\"NA\"}")
                        .with(csrf())
                        .with(user("student")
                                .authorities(new SimpleGrantedAuthority("student"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Ali")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roll_num", Matchers.is("bsef20")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.university", Matchers.is("pucit")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cgpa", Matchers.is("NA")));
    }

    //@Order(2)
    @Test
    public void testDeleteStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/student/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf())
                        .with(user("teacher")
                                .authorities(new SimpleGrantedAuthority("teacher"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    //@Order(2)
    @Test
    public void testPutStudent() throws Exception {
        // Create a new student first (assuming there's a POST endpoint to create students)
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/student")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"Ali\",\"roll_num\":\"bsef20\", \"university\":\"pucit\", \"cgpa\":\"NA\"}")
                        .with(csrf())
                        .with(user("student")
                                .authorities(new SimpleGrantedAuthority("student"))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()))
                .andReturn();

        // Extract the student ID from the response
        String content = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode studentJson = objectMapper.readTree(content);
        int studentId = studentJson.get("id").asInt(); // Use asInt() for Integer type

        // Perform the PUT request to update the student
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/student/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\":\"UpdatedName\",\"roll_num\":\"bsef21\", \"university\":\"pu\", \"cgpa\":\"3.5\"}")
                        .with(csrf())
                        .with(user("teacher")
                                .authorities(new SimpleGrantedAuthority("teacher"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(studentId))) // Use Matchers.is() for exact matching
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("UpdatedName")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roll_num", Matchers.is("bsef21")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.university", Matchers.is("pu")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cgpa", Matchers.is("3.5")));
    }
}

