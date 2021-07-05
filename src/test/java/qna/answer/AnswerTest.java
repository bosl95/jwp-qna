package qna.answer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;
import qna.question.Question;
import qna.question.QuestionTest;
import qna.user.User;
import qna.user.UserTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class AnswerTest {

    @Autowired
    private EntityManager entityManager;

    private Question question;

    @BeforeEach
    void setUp() {
        question = new Question("질문", "contents");
        question.writeBy(User.GUEST_USER);
        entityManager.persist(question);
    }

    @Test
    void answerToWriterTest() {
        // given
        Answer answer = new Answer(User.GUEST_USER, question, "answer");
        User writer = new User("pomo", "pomo@gmail.com", "password", "pomo@gmail.com");

        // when
        Answer registeredAnswer = answer.writeBy(writer);

        // then
        assertThat(writer.getAnswers()).contains(registeredAnswer);
    }

    @DisplayName("작성자를 입력하지 않은 경우 예외 처리")
    @Test
    void invalidWriter() {
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "답변"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("질문을 입력하지 않은 경우 예외 처리")
    @Test
    void invalidQuestion() {
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "답변"))
                .isInstanceOf(NotFoundException.class);
    }
}
