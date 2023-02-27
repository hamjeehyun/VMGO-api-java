package vmgo.store.entity.common;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity<T> {
    @Column(name = "created", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created;
    private LocalDateTime updated;

    /**
     * 생성자 (접근제한)
     */
    protected BaseTimeEntity() {
        this.created = LocalDateTime.now();
    }
    public BaseTimeEntity(T dto) {
        this.update(dto);
    }

    public abstract void update(T dto);

    public abstract T toDto();
}
