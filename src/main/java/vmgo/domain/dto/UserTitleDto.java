package vmgo.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vmgo.domain.common.BaseTimeDto;

@Getter
@NoArgsConstructor
public class UserTitleDto extends BaseTimeDto<UserTitleDto> {
    private String userTitleId;

    private UserDto user;

    private TitleDto title;

    @Builder
    public UserTitleDto(String userTitleId, UserDto user, TitleDto title) {
        this.userTitleId = userTitleId;
        this.user = user;
        this.title = title;
    }
}
