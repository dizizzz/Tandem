package box.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import box.app.model.question.Language;
import box.app.model.question.Question;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {
        "classpath:database/questions/add-questions.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:database/questions/remove-questions.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("Find the question by correct language - English, first page")
    public void findByLanguage_english_firstPage_returnQuestions() {
        Language language = Language.eng;
        Pageable pageable = PageRequest.of(0, 2);

        List<Question> questions = questionRepository.findByLanguage(language, pageable);

        assertFalse(questions.isEmpty());
        assertEquals(2, questions.size());
        assertEquals(language, questions.get(0).getLanguage());
    }

    @Test
    @DisplayName("Find questions by language - Ukrainian, no results")
    void findByLanguage_ukrainian_noResults_returnEmptyList() {
        Language language = Language.ukr;
        Pageable pageable = PageRequest.of(1, 9);

        List<Question> questions = questionRepository.findByLanguage(language, pageable);

        assertTrue(questions.isEmpty());
    }
}
