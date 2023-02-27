package vmgo.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vmgo.store.entity.Title;

import java.util.ArrayList;
import java.util.List;

@Setter
@SuperBuilder
@Getter
public class UserProfileDto extends UserDto {
    private String userProfileId;
    // 현재 장착 타이틀
    private TitleDto titleDto;
    // 보유 타이틀
    private List<TitleDto> titles;
    // 보유 별자리
    private ConstellationStatusDto constellations;
    // 최근 본 챌린지
    private ChallengeDto challenge;
    // 최근 본 영상
    private VideoDto video;

    public UserProfileDto(ChallengeDto challenge, VideoDto video, String userProfileId, TitleDto titleDto, List<TitleDto> titles, ConstellationStatusDto constellations, String recentChallenge, String recentVideo, String recentLink) {
        this.userProfileId = userProfileId;
        this.titleDto = titleDto;
        this.titles = titles;
        this.constellations = constellations;
        this.challenge = challenge;
        this.video = video;
    }


    public void setUserProfile(List<TitleDto> titleDtoList, ConstellationStatusDto constellationDto) {
        if (titleDtoList == null) {
            titleDtoList = new ArrayList<>();
        }

        this.titles = titleDtoList;
        this.constellations = constellationDto;
    }

    public String getRecentChallenge() {
        if (this.challenge != null) {
            return this.challenge.getTitle();
        }
        return null;
    }

    public String getRecentVideo() {
        if (this.video != null) {
            return this.video.getTitle();
        }
        return null;
    }

    public String getRecentLink() {
        String challengeId = "";
        String videoId = "";

        if (this.challenge != null) {
            challengeId =  this.challenge.getChallengeId();
        }
        if (this.video != null) {
            videoId =  this.video.getVideoId();
        }

        return "challenge/" + challengeId + '/' + videoId;
    }

    public String getTitle() {
        return this.titleDto.getTitleName();
    }
}
