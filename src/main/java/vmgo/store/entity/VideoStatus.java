package vmgo.store.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;
import vmgo.domain.dto.ChallengeDto;
import vmgo.domain.dto.VideoStatusDto;
import vmgo.domain.dto.UserDto;
import vmgo.domain.dto.VideoDto;
import vmgo.store.entity.common.BaseTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table
public class VideoStatus extends BaseTimeEntity<VideoStatusDto> {
    @Id @Column(name = "video_status_id", length = 100)
    @GeneratedValue(generator = "video_status_uuid")
    @GenericGenerator(name = "video_status_uuid", strategy = "uuid2")
    private String videoStatusId;
    private boolean status;
    private String uid;
    private String videoId;
    private String videoTitle;

    public VideoStatus() {
        super();
    }

    public VideoStatus(VideoStatusDto videoStatusDto) {
        super(videoStatusDto);
    }

    @Override
    public void update(VideoStatusDto dto) {
        BeanUtils.copyProperties(dto, this);
        if (dto.getUser() != null) {
            uid = dto.getUser().getUid();
        }
        if (dto.getVideo() != null) {
            videoId = dto.getVideo().getVideoId();
            videoTitle = dto.getVideo().getTitle();
        }
    }

    @Override
    public VideoStatusDto toDto() {
        return VideoStatusDto.builder()
                .videoStatusId(videoStatusId)
                .status(status)
                .user(UserDto.builder().uid(uid).build())
                .video(VideoDto.builder().videoId(videoId).title(videoTitle).build())
                .created(getCreated())
                .updated(getUpdated())
                .build();
    }
}