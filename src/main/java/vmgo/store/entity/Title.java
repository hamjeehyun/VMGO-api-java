package vmgo.store.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;
import vmgo.domain.dto.TitleDto;
import vmgo.store.entity.common.BaseTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @packageName vmgo.store.entity
 * @fileName Title.java
 * @author ssing_world
 * @date 2022/07/28
 * @description 타이틀 엔티티
 * ================================
 * DATE				AUTHOR			NOTE
 * 2022/07/28       ssing_world		최초생성
 */
@Setter
@Getter
@Entity
@Table
public class Title extends BaseTimeEntity<TitleDto> {
    @Id @Column(name = "title_id", length = 100)
    @GeneratedValue(generator = "title_uuid")
    @GenericGenerator(name = "title_uuid", strategy = "uuid2")
    private String titleId;
    private String challengeId;
    private String titleName;

    public Title() {
    }

    public Title(String titleId) {
        this.setTitleId(titleId);
    }

    public Title(TitleDto titleDto) {
        super(titleDto);
    }

    @Override
    public void update(TitleDto dto) {
        BeanUtils.copyProperties(dto, this);
    }

    @Override
    public TitleDto toDto() {
        return TitleDto.builder()
                .titleId(titleId)
                .challengeId(challengeId)
                .titleName(titleName)
                .build();
    }
}
