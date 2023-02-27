package vmgo.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import vmgo.domain.common.BaseTimeDto;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
public class VideoStatusDto extends BaseTimeDto<VideoStatusDto> {
    private String videoStatusId;
    // 비디오 시청 진행 상태 : true-완료 / false-진행중
    private boolean status;
    private UserDto user;
    private VideoDto video;

    public VideoStatusDto(String videoStatusId, boolean status, UserDto user, VideoDto video, LocalDateTime created, LocalDateTime updated) {
        this.videoStatusId = videoStatusId;
        this.status = status;
        this.user = user;
        this.video = video;
        this.created = created;
        this.updated = updated;
    }
}
