package vmgo.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import vmgo.domain.common.BaseTimeDto;

@Getter
@NoArgsConstructor
@SuperBuilder
public class ChallengeVideoDto extends BaseTimeDto<ChallengeVideoDto> {
    private String challengeVideoId;
    private VideoDto video;
    private ChallengeDto challenge;

    public ChallengeVideoDto(String challengeVideoId, VideoDto video, ChallengeDto challenge){
        this.challengeVideoId = challengeVideoId;
        this.video = video;
        this.challenge = challenge;
    }

    public String getVideoId() {
        return video.getVideoId();
    }
}
