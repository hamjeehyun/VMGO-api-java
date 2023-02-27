package vmgo.domain.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import vmgo.domain.common.BaseTimeDto;

@Getter
public class VideoDto extends BaseTimeDto<VideoDto> {
    private String videoId;
    private String title;
    private String date;
    private long duration;
    private List<String> member;
    private List<String> guest;
    private List<String> tag;
    private String description;
    private String thumbnail;
    private int views;
    private String vLiveLink;
    private UserStatus userStatus;
    private Map<Reaction, Object> reactions;
    private List<VideoReactionDto> userReactions;

    @Builder
    public VideoDto(String videoId, String title, String date, long duration, List<String> member, List<String> guest, List<String> tag, String description, String thumbnail, int views, String vLiveLink, Map<Reaction,Object> reactions, List<VideoReactionDto> userReactions) {
        this.videoId = videoId;
        this.title = title;
        this.date = date;
        this.duration = duration;
        this.member = member;
        this.guest = guest;
        this.tag = tag;
        this.description = description;
        this.thumbnail = thumbnail;
        this.views = views;
        this.vLiveLink = vLiveLink;
        this.reactions = reactions;
        this.userReactions = userReactions;
    }

    public VideoDto() {}

    public void setReactions(Map<Reaction,Object> reactions) {
        if (reactions == null)  reactions = new HashMap<>();
        this.reactions = reactions;
    }
    
    public void setUserReactions(List<VideoReactionDto> userReactions) {
    	if ( userReactions == null ) userReactions = new ArrayList<>();
    	this.userReactions = userReactions;
    }

    public void setUserStatusNone() {
        this.userStatus = UserStatus.NONE;
    }
    public void setUserStatusWatching() {
        this.userStatus = UserStatus.WATCHING;
    }
    public void setUserStatusComplete() {
        this.userStatus = UserStatus.COMPLETE;
    }
}
