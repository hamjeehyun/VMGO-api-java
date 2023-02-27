package vmgo.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vmgo.domain.common.BaseTimeDto;
import vmgo.store.entity.VideoReaction;

@Getter
@Setter
public class VideoReactionDto extends BaseTimeDto<VideoReactionDto> {
    /** 비디오 리액션 ID */
    private String videoReactionId;
    /** 리액션 */
    private Reaction reaction;
    /** 비디오 ID */
    private String videoId;
    /** 유저 ID */
    private String uid;
    
    @Builder
    private VideoReactionDto(String videoReactionId, Reaction reaction, String videoId, String uid) {
        this.videoReactionId = videoReactionId;
        this.reaction = reaction;
        this.videoId = videoId;
        this.uid = uid;
    }
    
    public VideoReactionDto(String videoId, String uid, Reaction reaction) {
    	this.videoId = videoId;
    	this.uid = uid;
    	this.reaction = reaction;
    }
}