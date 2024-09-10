package box.app.repository;

import box.app.model.question.Language;
import box.app.model.question.Question;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByLanguage(Language language, Pageable pageable);
}
