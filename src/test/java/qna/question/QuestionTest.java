package qna.question;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.answer.Answer;
import qna.user.User;
import qna.user.UserTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private QuestionRepository questionRepository;

    private User javajigi;
    private Question question;

    @BeforeEach
    void setUp() {
        javajigi = new User("javajigi", "password", "name", "javajigi@slipp.net");
        entityManager.persist(javajigi);
        question = new Question("title", "content").writeBy(javajigi);
    }

    @Test
    void questionToWriterTest() {
        // given
        User writer = new User("pomo", "pomo@gmail.com", "password", "pomo@gmail.com");

        // when
        Question question = new Question("title", "contents").writeBy(writer);

        // then
        assertThat(writer.getQuestions()).containsExactly(question);
    }

    @Test
    void answerToQuestionTest() {
        // given
        Answer answer = new Answer(javajigi, question, "answer");
        Answer answer2 = new Answer(javajigi, question, "answer");

        // when
        List<Answer> answers = question.getAnswers();

        // then
        assertThat(answers).contains(answer, answer2);
    }

    @DisplayName("question의 delete를 수행하면 deleted=false로 바뀐다.")
    @Test
    void softDeleteTest() {
        // when
        questionRepository.save(question);
        questionRepository.delete(question);
        entityManager.flush();

        // then
        assertThat(question.isDeleted()).isFalse();
    }

    @Test
    void ownerTest() {
        assertThat(question.isOwner(javajigi)).isTrue();
        assertThat(question.isOwner(User.GUEST_USER)).isFalse();
    }
}
