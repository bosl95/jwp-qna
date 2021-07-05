package qna.deletehistory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import qna.ContentType;
import qna.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long contentId;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @CreatedDate
    private LocalDateTime createDate;

    @OneToOne
    @JoinColumn(name = "DELETED_BY_ID", foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedByUser;

    public DeleteHistory(ContentType contentType, Long contentId, User deletedByUser, LocalDateTime createDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedByUser = deletedByUser;
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deletedByUser, that.deletedByUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedByUser);
    }
}
