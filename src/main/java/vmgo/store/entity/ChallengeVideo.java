package vmgo.store.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;
import vmgo.domain.dto.ChallengeDto;
import vmgo.domain.dto.ChallengeVideoDto;
import vmgo.domain.dto.VideoDto;
import vmgo.store.entity.common.BaseTimeEntity;
import vmgo.util.JsonUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table
public class ChallengeVideo extends BaseTimeEntity<ChallengeVideoDto> {
    @Id @Column(name = "challenge_video_id", length = 100)
    @GeneratedValue(generator = "challenge_video_uuid")
    @GenericGenerator(name = "challenge_video_uuid", strategy = "uuid2")
    private String challengeVideoId;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    public ChallengeVideo() {
        super();
    }

    public ChallengeVideo(ChallengeVideoDto challengeVideoDto) {
        super(challengeVideoDto);
    }

    @Override
    public void update(ChallengeVideoDto dto) {
        BeanUtils.copyProperties(dto, this);
        if (dto.getVideo() != null) {
            Video video = new Video(dto.getVideo());
            this.video = video;
        }
        if (dto.getChallenge() != null) {
            Challenge challenge = new Challenge(dto.getChallenge());
            this.challenge = challenge;
        }

    }

    @Override
    public ChallengeVideoDto toDto() {
        return ChallengeVideoDto.builder()
                .challengeVideoId(challengeVideoId)
                .challenge(ChallengeDto.builder().challengeId(challenge.getChallengeId()).build())
                .video(VideoDto.builder().videoId(video.getVideoId()).build())
                .build();
    }

    public VideoDto toVideoDto() {
        ChallengeVideoDto challengeVideoDto = ChallengeVideoDto.builder()
                .video(VideoDto.builder()
                        .videoId(video.getVideoId())
                        .title(video.getTitle())
                        .date(video.getDate())
                        .duration(video.getDuration())
                        .member(JsonUtil.fromJsonList(video.getMember(), String.class))
                        .guest(JsonUtil.fromJsonList(video.getGuest(), String.class))
                        .tag(JsonUtil.fromJsonList(video.getTag(), String.class))
                        .description(video.getDescription())
                        .thumbnail(video.getThumbnail())
                        .views(video.getViews())
                        .vLiveLink(video.getVLiveLink())
                        .build())
                .build();

        return challengeVideoDto.getVideo();
    }
}
