package vmgo.domain.common;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeDto<T> {
    protected LocalDateTime created;
    protected LocalDateTime updated;

    public BaseTimeDto() {
    }

    public BaseTimeDto(LocalDateTime created, LocalDateTime updated) {
        this.created = created;
        this.updated = updated;
    }
}
