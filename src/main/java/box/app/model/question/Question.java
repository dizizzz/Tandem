package box.app.model.question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false, name = "short_answer")
    private String shortAnswer;

    @Column(nullable = false, name = "full_answer")
    private String fullAnswer;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar",
            nullable = false)
    private Language language;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}
