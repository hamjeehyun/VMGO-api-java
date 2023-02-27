package vmgo.service.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vmgo.domain.dto.ChallengeDto;
import vmgo.domain.dto.ChallengeStatusDto;
import vmgo.domain.dto.ChallengeVideoDto;
import vmgo.domain.dto.ResponseDto;
import vmgo.domain.dto.TitleDto;
import vmgo.domain.dto.UserDto;
import vmgo.domain.dto.UserProfileDto;
import vmgo.domain.dto.VideoDto;
import vmgo.domain.dto.VideoStatusDto;
import vmgo.service.ChallengeService;
import vmgo.store.ChallengeStatusStore;
import vmgo.store.ChallengeStore;
import vmgo.store.ChallengeVideoStore;
import vmgo.store.TitleStore;
import vmgo.store.UserProfileStore;
import vmgo.store.VideoStatusStore;
import vmgo.util.ExceptionUtil;
import vmgo.util.JsonUtil;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChallengeServiceLogic implements ChallengeService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ChallengeServiceLogic.class);

    private final ChallengeStore challengeStore;
    private final ChallengeVideoStore challengeVideoStore;
    private final ChallengeStatusStore challengeStatusStore;
    private final VideoStatusStore videoStatusStore;
    private final TitleStore titleStore;
    private final UserProfileStore userProfileStore;

    public ChallengeServiceLogic(ChallengeStore challengeStore, ChallengeVideoStore challengeVideoStore, ChallengeStatusStore challengeStatusStore, VideoStatusStore videoStatusStore, TitleStore titleStore, UserProfileStore userProfileStore) {
        this.challengeStore = challengeStore;
        this.challengeVideoStore = challengeVideoStore;
        this.challengeStatusStore = challengeStatusStore;
        this.videoStatusStore = videoStatusStore;
        this.titleStore = titleStore;
        this.userProfileStore = userProfileStore;
    }

    @Override
    public ResponseDto registerChallengeAndChallengeVideo(ChallengeDto challengeDto) {
        // 챌린지 총 길이 구하기
        long duration = 0;
        for (VideoDto videoDto : challengeDto.getVideos()) {
            duration += videoDto.getDuration();
        }
        challengeDto.setDuration(duration);

        // 챌린지 등록
        String challengeId = challengeStore.saveChallenge(challengeDto);

        // 타이틀 등록
        titleStore.saveTitle(
                TitleDto.builder()
                        .titleName(challengeDto.getTitle())
                        .challengeId(challengeId)
                        .build());

        for (VideoDto videoDto : challengeDto.getVideos()) {
            duration += videoDto.getDuration();
            // 챌린지-비디오 등록
            ChallengeVideoDto challengeVideoDto = ChallengeVideoDto.builder()
                    .video(VideoDto.builder().videoId(videoDto.getVideoId()).build())
                    .challenge(ChallengeDto.builder().challengeId(challengeId).build())
                    .build();
            challengeVideoStore.saveChallengeVideo(challengeVideoDto);
        }

        return new ResponseDto(challengeId);
    }

    @Override
    public void startChallengeByUid(String challengeId, String uid) {
        // 챌린지 상태
        challengeStatusStore.saveChallengeStatus(
                ChallengeStatusDto.builder()
                        .challenge(ChallengeDto.builder().challengeId(challengeId).build())
                        .user(UserDto.builder().uid(uid).build())
                        .status(false)
                        .build()
        );

        // userProfile 수정
        UserProfileDto userProfileDto = userProfileStore.findUserProfileByUid(uid);

        if (userProfileDto == null)
            throw ExceptionUtil.createOnfBizException("ONF_0001", "사용자 프로필");

        userProfileStore.saveUserProfileAndUid(
                UserProfileDto.builder()
                        .userProfileId(userProfileDto.getUserProfileId())
                        .titleDto(userProfileDto.getTitleDto())
                        .titles(userProfileDto.getTitles())
                        .constellations(userProfileDto.getConstellations())
                        .challenge(ChallengeDto.builder().challengeId(challengeId).build())
                        .video(null) // 비디오 정보 없음
                        .build()
                , uid
        );
    }

    @Override
    public ResponseDto updateChallengeAndChallengeVideo(ChallengeDto challengeDto) {
        long duration = 0;
        for (VideoDto videoDto : challengeDto.getVideos()) {
            duration += videoDto.getDuration();
        }
        challengeDto.setDuration(duration);

        // 챌린지 수정
        String challengeId = challengeStore.saveChallenge(challengeDto);

        // 타이틀 수정
        TitleDto titleDto = titleStore.findTitleByChallengeId(challengeId);
        titleStore.saveTitle(
                TitleDto.builder()
                        .titleId(titleDto.getTitleId())
                        .titleName(challengeDto.getTitle())
                        .challengeId(challengeId)
                        .build());

        // 챌린지-비디오 초기화
        challengeVideoStore.deleteByChallengeId(challengeId);
        for (VideoDto videoDto : challengeDto.getVideos()) {
            duration += videoDto.getDuration();
            // 챌린지-비디오 등록
            ChallengeVideoDto challengeVideoDto = ChallengeVideoDto.builder()
                    .video(VideoDto.builder().videoId(videoDto.getVideoId()).build())
                    .challenge(ChallengeDto.builder().challengeId(challengeId).build())
                    .build();
            challengeVideoStore.saveChallengeVideo(challengeVideoDto);
        }

        return new ResponseDto(challengeId);
    }

    @Override
    public List<ChallengeDto> findAllChallenge(List<String> tags) {
        List<ChallengeDto> challengeDtoList = challengeStore.findAllChallenge(tags);

        return this.setChallengeList(challengeDtoList);
    }

    @Override
    public List<ChallengeDto> findAllChallengeActiveIsTrue(List<String> tags) {
        List<ChallengeDto> challengeDtoList = challengeStore.findAllChallengeActiveIsTrue(tags);

        return this.setChallengeList(challengeDtoList);
    }

    @Override
    public List<ChallengeDto> findAllChallengeActiveIsTrueByUid(String uid, List<String> tags) {
        // 활성화 된 모든 챌린지 목록 불러오기
        List<ChallengeDto> challengeDtoList = challengeStore.findAllChallengeActiveIsTrue(tags);

        return challengeDtoList.stream().peek(challengeDto -> {
            String challengeId = challengeDto.getChallengeId();
            int userCount = challengeStatusStore.countByChallengeId(challengeId);
            int videoCount = challengeVideoStore.countByChallengeId(challengeId);
            ChallengeStatusDto challengeStatusDto = challengeStatusStore.findChallengeStatusByChallengeIdAndUid(challengeId, uid);

            if (challengeStatusDto != null) {
                boolean challengeStatus = challengeStatusDto.isStatus();
                if (challengeStatus) {
                    // 챌린지 완료
                    challengeDto.setUserStatusComplete();
                } else {
                    // 챌린지 시청중
                    challengeDto.setUserStatusWatching();
                }
            } else {
                // challengeStatusDto가 null인 경우에는 참여한 적 없는 비디오
                challengeDto.setUserStatusNone();
            }

            challengeDto.setUserCount(userCount);
            challengeDto.setVideoCount(videoCount);
        }).collect(Collectors.toList());
    }

    @Override
    public List<ChallengeDto> findAllChallengeByUidForDashboard(String uid) {
        // 사용자가 참여중인 챌린지 조회
        List<ChallengeStatusDto> challengeStatusDtoList = challengeStatusStore.findAllChallengeStatusByUidAndStatusIsFalse(uid);
        List<ChallengeDto> challengeDtoList = challengeStatusDtoList.stream().map(ChallengeStatusDto::getChallenge).collect(Collectors.toList());

        return challengeDtoList.stream().map(challengeDto -> {
            String challengeId = challengeDto.getChallengeId();
            // 챌린지에 속한 비디오 조회
            ChallengeDto challenge = this.findChallengeByIdAndUid(challengeId, uid);

            ChallengeStatusDto challengeStatusDto = challengeStatusStore.findChallengeStatusByChallengeIdAndUid(challengeId, uid);

            if (challengeStatusDto != null) {
                boolean challengeStatus = challengeStatusDto.isStatus();
                if (challengeStatus) {
                    // 챌린지 완료
                    challenge.setUserStatusComplete();
                } else {
                    // 챌린지 시청중
                    challenge.setUserStatusWatching();
                }
            } else {
                // challengeStatusDto가 null인 경우에는 참여한 적 없는 비디오
                challenge.setUserStatusNone();
            }

            int userCount = challengeStatusStore.countByChallengeId(challengeId);
            int videoCount = challenge.getVideos().size();

            challenge.setUserCount(userCount);
            challenge.setVideoCount(videoCount);

            return challenge;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ChallengeDto> findAllChallengeActiveIsTrueAndFeaturedIdTrue(List<String> tags) {
        List<ChallengeDto> challengeDtoList = challengeStore.findAllChallengeActiveIsTrueAndFeaturedIdTrue(tags);

        return this.setChallengeList(challengeDtoList);
    }

    @Override
    public ChallengeDto findChallengeById(String id) {
        ChallengeDto challengeDto = challengeStore.findChallengeWithoutVideoById(id);

        if (challengeDto == null ) throw ExceptionUtil.createOnfBizException("ONF_0001", "챌린지");

        List<VideoDto> videoDtoList = challengeVideoStore.findAllVideoByChallengeId(id);
        challengeDto.setVideos(videoDtoList);

        return challengeDto;
    }

    @Override
    public ChallengeDto findChallengeByIdAndUid(String id, String uid) {
        // 챌린지 상세조회
        ChallengeDto challengeDto = this.findChallengeById(id);

        // 비디오의 상태 넣기
        List<VideoDto> videoDtoList = challengeDto.getVideos();

        videoDtoList.forEach(videoDto -> {
            VideoStatusDto videoStatusDto = videoStatusStore.findVideoStatusByUidAndVideoId(uid, videoDto.getVideoId());

            if (videoStatusDto != null) {
                boolean videoStatus = videoStatusDto.isStatus();
                if (videoStatus) {
                    // 비디오 시청 완료
                    videoDto.setUserStatusComplete();
                } else {
                    // 비디오 시청중
                    videoDto.setUserStatusWatching();
                }
            } else {
                // videoStatusDto 가 null인 경우에는 참여한 적 없는 비디오
                videoDto.setUserStatusNone();
            }
        });

        // 챌린지 상태 넣기
        ChallengeStatusDto challengeStatusDto = challengeStatusStore.findChallengeStatusByChallengeIdAndUid(id, uid);

        if (challengeStatusDto != null) {
            boolean challengeStatus = challengeStatusDto.isStatus();
            if (challengeStatus) {
                // 챌린지 완료
                challengeDto.setUserStatusComplete();
            } else {
                // 챌린지 시청중
                challengeDto.setUserStatusWatching();
            }
        } else {
            // challengeStatusDto가 null인 경우에는 참여한 적 없는 비디오
            challengeDto.setUserStatusNone();
        }

        return challengeDto;
    }

    @Override
    public void updateChallengeDuration() {
        List<ChallengeDto> challengeDtoList = challengeStore.findAllChallengeAndVideo(null);

        for(ChallengeDto challengeDto : challengeDtoList) {
            long challengeDuration = 0;
            for(VideoDto videoDto : challengeDto.getVideos()) {
                challengeDuration += videoDto.getDuration();
            }

            challengeStore.saveChallenge(ChallengeDto.builder()
                    .challengeId(challengeDto.getChallengeId())
                    .duration(challengeDuration)
                    .description(challengeDto.getDescription())
                    .featured(challengeDto.isFeatured())
                    .active(challengeDto.isActive())
                    .tag(challengeDto.getTag())
                    .thumbnail(challengeDto.getThumbnail())
                    .title(challengeDto.getTitle())
                    .build());
        }
    }

    private List<ChallengeDto> setChallengeList(List<ChallengeDto> challengeDtoList) {
        return challengeDtoList.stream().peek(challengeDto -> {
            int userCount = challengeStatusStore.countByChallengeId(challengeDto.getChallengeId());
            int videoCount = challengeVideoStore.countByChallengeId(challengeDto.getChallengeId());
            challengeDto.setUserCount(userCount);
            challengeDto.setVideoCount(videoCount);
        }).collect(Collectors.toList());
    }
}
