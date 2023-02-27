package vmgo.store.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;
import vmgo.domain.dto.ConstellationDto;
import vmgo.domain.dto.ConstellationStatusDto;
import vmgo.domain.dto.UserConstellationDto;
import vmgo.domain.dto.UserDto;
import vmgo.store.entity.common.BaseTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @packageName vmgo.store.entity
 * @fileName Constellation.java
 * @author ssing_world
 * @date 2022/07/28
 * @description 별자리 엔티티
 * ================================
 * DATE				AUTHOR			NOTE
 * 2022/07/28       ssing_world		최초생성
 * 2022/08/20		RUBY			생성자 추가
 */
@Setter
@Getter
@Entity
@Table
public class UserConstellation extends BaseTimeEntity<UserConstellationDto> {
    @Id @Column(name = "user_constellation", length = 100)
    @GeneratedValue(generator = "user_constellation_uuid")
    @GenericGenerator(name = "user_constellation_uuid", strategy = "uuid2")
    private String userConstellationId;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "constellation_id")
    private Constellation constellation;

    public UserConstellation() {}

    public UserConstellation(UserConstellationDto userConstellationDto) {
        super(userConstellationDto);
    }
    
    public UserConstellation(Constellation constellation, User user) {
    	this.constellation = constellation;
    	this.user = user;
    }

    @Override
    public void update(UserConstellationDto dto) {
        BeanUtils.copyProperties(dto, this);

        if (dto.getUser() != null) {
            User user = new User(dto.getUser());
            this.user = user;
        }

        if (dto.getConstellation() != null) {
            Constellation constellation = new Constellation(dto.getConstellation());
            this.constellation = constellation;
        }
    }

    @Override
    public UserConstellationDto toDto() {
        return UserConstellationDto.builder()
                .user(UserDto.builder().uid(user.getUid()).build())
                .constellation(ConstellationDto.builder().constellationId(constellation.getConstellationId()).build())
                .build();
    }
}
