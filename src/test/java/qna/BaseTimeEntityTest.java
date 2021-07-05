package qna;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.answer.Answer;
import qna.answer.AnswerRepository;
import qna.question.Question;
import qna.question.QuestionRepository;
import qna.user.User;
import qna.user.UserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BaseTimeEntityTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    private User javajigi;
    private Question question1;
    private Question question2;

    @BeforeEach
    void setUp() {
        javajigi = new User("javajigi", "password", "name", "javajigi@slipp.net");
        userRepository.save(javajigi);
        question1 = new Question("title1", "contents1").writeBy(javajigi);
        question2 = new Question("title2", "contents2").writeBy(javajigi);
        questionRepository.save(question1);
        questionRepository.save(question2);
    }


    @DisplayName("Answer을 저장하면 생성 시간과 수정 시간이 자동 생성")
    @Test
    void save() {
        // given
        Answer answer = new Answer(javajigi, question1, "answer contents");
        question1.addAnswer(answer);

        // when
        Answer savedAnswer = answerRepository.save(answer);
        entityManager.flush();

        // then
        assertThat(savedAnswer.getCreatedAt()).isNotNull();
        assertThat(savedAnswer.getUpdatedAt()).isNotNull();
    }

    @DisplayName("데이터 변경 시 lastModifiedDate가 수정된다")
    @Test
    void update() {
        // given
        Question question = new Question("title", "content").writeBy(javajigi);
        Answer answer = new Answer(javajigi, question, "answer");
        question.addAnswer(answer);
        Answer savedAnswer = answerRepository.save(answer);

        // when
        LocalDateTime updatedAt = savedAnswer.getUpdatedAt();
//        savedAnswer.toQuestion(question2); // TODO : 다른 걸로!
        entityManager.flush();

        // then
        LocalDateTime changedUpdatedAt = savedAnswer.getUpdatedAt();
        assertThat(updatedAt).isNotEqualTo(changedUpdatedAt);
    }

}