package box.app.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import box.app.dto.question.QuestionRequestDto;
import box.app.model.question.Language;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuestionControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private WebApplicationContext applicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/questions/add-questions.sql")
            );
        }
    }

    @AfterEach
    void teardown() {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/questions/remove-questions.sql")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Add the question")
    @Sql(
            scripts = "classpath:database/questions/remove-questions-by-shortAnswer.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void addQuestion_validRequestDto_success() throws Exception {
        QuestionRequestDto requestDto = new QuestionRequestDto()
                .setQuestion("Question 28")
                .setShortAnswer("Short Answer")
                .setFullAnswer("Full Answer")
                .setLanguage(Language.eng);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(
                        post("/questions")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.question", is(requestDto.getQuestion())));
    }

    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    @Test
    @DisplayName("Get all questions English questions")
    void getAllEng_givenQuestions_shouldReturnAllEngQuestions() throws Exception {
        mockMvc.perform(
                        get("/questions/eng")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id", is(28)))
                .andExpect(jsonPath("$[0].question",
                        is("IS IT POSSIBLE TO START BOXING AT ANY AGE?")))
                .andExpect(jsonPath("$[1].id", is(29)))
                .andExpect(jsonPath("$[1].question",
                        is("HOW DOES BOXING AFFECT HEALTH? ARE THERE ANY CONTRAINDICATIONS?")));

    }

    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    @Test
    @DisplayName("Get all questions Ukrainian questions")
    void getAllUkr_givenQuestions_shouldReturnAllUkrQuestions() throws Exception {
        mockMvc.perform(
                        get("/questions/ukr")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    @Test
    @DisplayName("Get all questions German questions")
    void getAllDeu_givenQuestions_shouldReturnAllDeuQuestions() throws Exception {
        mockMvc.perform(
                        get("/questions/deu")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)));

    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update question by ID")
    void updateQuestion_givenId_shouldReturnQuestion() throws Exception {
        Long updateId = 28L;
        QuestionRequestDto updateQuestion = new QuestionRequestDto()
                .setQuestion("Question new")
                .setShortAnswer("Short Answer new")
                .setFullAnswer("Full Answer new")
                .setLanguage(Language.eng);

        mockMvc.perform(
                        put("/questions/{id}", updateId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateQuestion))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question", is(updateQuestion.getQuestion())));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete a question")
    @Sql(
            scripts = "classpath:database/questions/add-questions-to-delete.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void delete_validCarId_success() throws Exception {
        mockMvc.perform(
                        delete("/questions/{id}", 32L)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }
}
