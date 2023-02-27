package vmgo.store.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;
import vmgo.domain.dto.ChallengeDto;
import vmgo.domain.dto.TitleDto;
import vmgo.domain.dto.UserProfileDto;
import vmgo.domain.dto.VideoDto;
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
 * @fileName UserProfile.java
 * @author ssing_world
 * @date 2022/07/28
 * @description 사용자 프로필 엔티티
 * ================================
 * DATE				AUTHOR			NOTE
 * 2022/07/28       ssing_world		최초생성
 * 2022/08/27       ssing_world		비디오/챌린지 추가
 */
@Setter
@Getter
@Entity
@Table
public class UserProfile extends BaseTimeEntity<UserProfileDto> {
    @Id @Column(name = "user_profile_id", length = 100)
    @GeneratedValue(generator = "user_profile_uuid")
    @GenericGenerator(name = "user_profile_uuid", strategy = "uuid2")
    private String userProfileId;
    private String uid;
    private String titleId;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    public UserProfile() {

    }

    public UserProfile(UserProfileDto userProfileDto) {
        super(userProfileDto);

    }

    @Override
    public void update(UserProfileDto dto) {
        BeanUtils.copyProperties(dto, this);

        if (dto.getTitleDto() != null) {
            Title title = new Title(dto.getTitleDto().getTitleId());
            this.titleId = title.getTitleId();
        }
        if (dto.getChallenge() != null) {
            Challenge challenge = new Challenge(dto.getChallenge());
            this.challenge = challenge;
        }
        if (dto.getVideo() != null) {
            Video video = new Video(dto.getVideo());
            this.video = video;
        }
    }
    @Override
    public UserProfileDto toDto() {
        return UserProfileDto.builder()
                .userProfileId(userProfileId)
                .titleDto(TitleDto.builder().titleId(titleId).build())
                .challenge(challenge != null ?
                        ChallengeDto.builder()
                                .challengeId(challenge.getChallengeId())
                                .title(challenge.getTitle())
                                .build()
                        :
                        null)
                .video(video != null ? VideoDto.builder().videoId(video.getVideoId()).title(video.getTitle()).build() : null)
                .build();
    }
}
