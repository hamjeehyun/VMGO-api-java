package vmgo.store.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;
import vmgo.domain.dto.UserDto;
import vmgo.domain.dto.UserRole;
import vmgo.store.entity.common.BaseTimeEntity;

@Setter
@Getter
@Entity
@Table
public class User extends BaseTimeEntity<UserDto> {
    @Id @Column(name = "uid", length = 100)
    private String uid;
    /** 지역 */
    private String region;
    /** 별명 */
    private String nickName;
    /** 사용자 아이디 */
    private String userId;
    /** twitter, google, email, ... */
    private String method;
    /** 유저 권한 */
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User() {
        super();
    }
    public User(String uid) {
        this.setUid(uid);
    }

    public User(UserDto userDto) {
        super(userDto);
    }

    @Override
    public void update(UserDto dto) {
        BeanUtils.copyProperties(dto, this);
    }

    @Override
    public UserDto toDto() {
        return UserDto.builder()
                .uid(uid)
                .region(region)
                .nickName(nickName)
                .userId(userId)
                .method(method)
                .userRole(userRole)
                .build();
    }
}
