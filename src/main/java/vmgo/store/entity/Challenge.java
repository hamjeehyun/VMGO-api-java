package vmgo.store.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import vmgo.domain.dto.ChallengeDto;
import vmgo.store.entity.common.BaseTimeEntity;
import vmgo.util.JsonUtil;
import vmgo.util.StringListConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@Table
public class Challenge extends BaseTimeEntity<ChallengeDto> {
    @Id @Column(name = "challenge_id", length = 100)
    private String challengeId;
    private String title;
    private String description;
    private String tag;
    private long duration;
    private String thumbnail;
    private boolean featured;
    private boolean active;

    @OneToMany(mappedBy = "challenge", fetch= FetchType.LAZY)
    private List<ChallengeVideo> challengeVideos = new ArrayList<>();

    public Challenge() {
        super();
    }

    public Challenge(ChallengeDto challengeDto) {
        super(challengeDto);
    }

    public Challenge(String challengeId) {
        this.setChallengeId(challengeId);
    }

    @Override
    public void update(ChallengeDto dto) {
        BeanUtils.copyProperties(dto, this);
        if (dto.getTag() != null) {
            tag = JsonUtil.toJson(dto.getTag());
        }
    }

    @Override
    public ChallengeDto toDto() {
        List<Video> videos = challengeVideos.stream().map(ChallengeVideo::getVideo).collect(Collectors.toList());
        return ChallengeDto.builder()
                .challengeId(challengeId)
                .title(title)
                .description(description)
                .tag(JsonUtil.fromJsonList(tag, String.class))
                .duration(duration)
                .thumbnail(thumbnail)
                .featured(featured)
                .active(active)
                .videos(videos.stream().map(Video::toDto).collect(Collectors.toList()))
                .build();
    }

    public ChallengeDto toListDto() {
        return ChallengeDto.builder()
                .challengeId(challengeId)
                .title(title)
                .description(description)
                .tag(JsonUtil.fromJsonList(tag, String.class))
                .duration(duration)
                .thumbnail(thumbnail)
                .featured(featured)
                .active(active)
                .videos(new ArrayList<>())
                .build();
    }
}
