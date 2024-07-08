
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project02.Project03Application;
import com.practice.project02.student.Student;
import com.sun.source.tree.ModuleTree;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;




@AutoConfigureMockMvc
@SpringBootTest(classes = Project03Application.class)
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
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student/30"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "admin", username = "admin")
    public void testUpdateStudent() throws Exception {
        // Mock data
        Long studentId = 20L;
        Student student = new Student();
        student.setId(studentId);
        student.setName("Ahmad");
        student.setRoll_num("Bsef20");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/student/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Ahmad"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roll_num").value("Bsef20"));
    }

    @Test
    @WithMockUser(roles = "admin", username = "admin")
    public void testDeleteStudent() throws Exception {
        Long studentId = 1L;

        // Perform DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/student/{id}", studentId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(" Student deleted successfully"));
    }
}

