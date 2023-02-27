package vmgo.store.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;
import vmgo.domain.dto.Reaction;
import vmgo.domain.dto.VideoReactionDto;
import vmgo.store.entity.common.BaseTimeEntity;

@Getter
@Setter
@Entity
@Table
public class VideoReaction extends BaseTimeEntity<VideoReactionDto> {
    /** 비디오 리액션 ID */
    @Id @Column(name = "video_reaction_id", length = 100)
    @GeneratedValue(generator = "video_reaction_id_uuid")
    @GenericGenerator(name = "video_reaction_id_uuid", strategy = "uuid2")
    private String videoReactionId;
    /** 리액션 */
    @Enumerated(EnumType.STRING)
    private Reaction reaction;
    /** 비디오 ID */
    private String videoId;
    /** 유저 ID */
    private String uid;

    public VideoReaction(VideoReactionDto videoReactionDto) {
        super(videoReactionDto);
    }

	public VideoReaction() {
	    super();
	}

    @Override
    public void update(VideoReactionDto dto) {
        BeanUtils.copyProperties(dto, this);
    }

    @Override
    public VideoReactionDto toDto() {
        VideoReactionDto videoReactionDto = VideoReactionDto.builder()
                .videoReactionId(videoReactionId)
                .reaction(reaction)
                .videoId(videoId)
                .uid(uid)
                .build();

        videoReactionDto.setCreated(getCreated());
        videoReactionDto.setUpdated(getUpdated());
        return videoReactionDto;
    }
}