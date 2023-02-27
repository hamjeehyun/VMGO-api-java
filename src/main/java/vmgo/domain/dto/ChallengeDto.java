package vmgo.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import vmgo.domain.common.BaseTimeDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ChallengeDto extends BaseTimeDto<ChallengeDto> {
    private String challengeId;
    private String title;
    private String description;
    private List<String> tag;
    private long duration;
    private String thumbnail;
    private boolean featured;
    private boolean active;
    private int userCount;
    private int videoCount;
    private UserStatus userStatus;
    private List<VideoDto> videos = new ArrayList<>();

    @Builder
    public ChallengeDto(String challengeId, String title, String description, List<String> tag, long duration, String thumbnail, boolean featured, boolean active, int userCount, int videoCount, UserStatus userStatus, List<VideoDto> videos) {
        this.challengeId = challengeId;
        this.title = title;
        this.description = description;
        this.tag = tag;
        this.duration = duration;
        this.thumbnail = thumbnail;
        this.featured = featured;
        this.active = active;
        this.userCount = userCount;
        this.videoCount = videoCount;
        this.userStatus = userStatus;
        this.videos = videos;
    }

    public void setUserCount(int count) {
        this.userCount = count;
    }
    public void setVideoCount(int count) {
        this.videoCount = count;
    }
    public void setDuration(long duration) {
        this.duration = duration;
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
    public void setVideos(List<VideoDto> videos) {
        this.videos = videos;
    }
    public double getChallengeProgress() {
        if (videos != null) {
            // 전체 개수 구하기
            double total = videos.size();
            double completeCount = 0;

            if (videos.size() > 0) {
                for (VideoDto videoDto : videos) {
                    if (videoDto.getUserStatus() != null) {
                        // 완료 개수 구하기
                        if (videoDto.getUserStatus().equals(UserStatus.COMPLETE)) {
                            completeCount++;
                            continue;
                        }
                    }
                }
                // 진행률 구하기
                return  completeCount/total;
            }
        }
        return 0;
    }
}
