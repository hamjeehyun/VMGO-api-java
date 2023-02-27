package vmgo.domain.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import vmgo.domain.common.BaseTimeDto;

@SuperBuilder
@Getter
@ToString
@NoArgsConstructor
public class UserDto extends BaseTimeDto<UserDto> {
    /** firebase에서 관리하고 있는 사용자 id이자 user의 PK */
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
    private UserRole userRole;
    
    public UserDto(String userId, UserRole userRole) {
    	this.userId = userId;
    	this.userRole = userRole;
    }

    public UserDto(String uid, String region, String nickName, String userId, String method, UserRole userRole) {
        this.uid = uid;
        this.region = region;
        this.nickName = nickName;
        this.userId = userId;
        this.method = method;
        this.userRole = userRole;
    }

    public void setUser(UserDto user){
        this.uid = user.getUid();
        this.region = user.getRegion();
        this.nickName = user.getNickName();
        this.userId = user.getUserId();
        this.method = user.getMethod();
        this.userRole = user.getUserRole();
    }

    public void setLoginDate() {
        this.updated = LocalDateTime.now();
    }
    
    public void setUserRole(UserRole userRole) {
    	this.userRole = userRole;
    }
}
