package qna.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.question.Question;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserTest {
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private Question question;

    @BeforeEach
    void setUp() {
        question = new Question("질문", "질문 내용");
        entityManager.persist(question);
    }

    @Test
    void save() {
        // given
        User pomo = new User("pomo", "password", "name", "pomo@naver.com");

        // when
        User savedUser = userRepository.save(pomo);
        User findUser = userRepository.findById(savedUser.getId()).get();

        // then
        assertThat(savedUser).isEqualTo(findUser);
    }
}
