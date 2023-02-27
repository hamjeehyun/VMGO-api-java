package vmgo.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vmgo.domain.common.BaseTimeDto;

@Getter
@NoArgsConstructor
public class ChallengeStatusDto extends BaseTimeDto<ChallengeStatusDto> {
    private String challengeStatusId;
    private ChallengeDto challenge;
    private UserDto user;
    private boolean status;

    @Builder
    public ChallengeStatusDto(String challengeStatusId, ChallengeDto challenge, UserDto user, boolean status) {
        this.challengeStatusId = challengeStatusId;
        this.challenge = challenge;
        this.user = user;
        this.status = status;
    }

    public void setComplete() {
        this.status = true;
    }
}
