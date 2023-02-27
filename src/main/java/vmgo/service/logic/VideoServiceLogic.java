package vmgo.service.logic;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import vmgo.domain.dto.ChallengeDto;
import vmgo.domain.dto.ChallengeStatusDto;
import vmgo.domain.dto.UserDto;
import vmgo.domain.dto.UserProfileDto;
import vmgo.domain.dto.VideoDto;
import vmgo.domain.dto.VideoReactionDto;
import vmgo.domain.dto.VideoStatusDto;
import vmgo.service.VideoService;
import vmgo.store.ChallengeStatusStore;
import vmgo.store.ChallengeStore;
import vmgo.store.UserProfileStore;
import vmgo.store.VideoReactionStore;
import vmgo.store.VideoStatusStore;
import vmgo.store.VideoStore;
import vmgo.util.ExceptionUtil;

@Service
public class VideoServiceLogic implements VideoService {
    private final static Logger LOGGER = LoggerFactory.getLogger(VideoServiceLogic.class);

    private final VideoStore videoStore;
    private final ChallengeStore challengeStore;
    private final VideoStatusStore videoStatusStore;
    private final ChallengeStatusStore challengeStatusStore;
    private final VideoReactionStore videoReactionStore;
    private final UserProfileStore userProfileStore;

    public VideoServiceLogic(VideoStore videoStore, ChallengeStore challengeStore, VideoStatusStore videoStatusStore, ChallengeStatusStore challengeStatusStore, VideoReactionStore videoReactionStore, UserProfileStore userProfileStore) {
        this.videoStore = videoStore;
        this.challengeStore = challengeStore;
        this.videoStatusStore = videoStatusStore;
        this.challengeStatusStore = challengeStatusStore;
        this.videoReactionStore = videoReactionStore;
        this.userProfileStore = userProfileStore;
    }

    @Override
    public List<VideoDto> findAllVideo() {
        return videoStore.findAllVideo();
    }

    @Override
    public VideoDto findVideoById(String id) {
        VideoDto videoDto = videoStore.findVideoById(id);

        if (videoDto == null) throw ExceptionUtil.createOnfBizException("ONF_0001", "비디오");

        return videoDto;
    }

    @Override
    public VideoDto findVideoByIdAndRegisterStatus(String videoId, String uid, String challengeId) {
        VideoStatusDto videoStatusDto = videoStatusStore.findVideoStatusByUidAndVideoId(uid, videoId);
        // 비디오 조회
        VideoDto videoDto = this.findVideoById(videoId);

        // 비디오 상태 업테이트
        this.createVideoStatus(videoDto, videoStatusDto, uid);
        // 챌린지 상태 업데이트
        this.createChallengeStatus(uid, challengeId);
        // 최근 본 챌린지-비디오 업데이트
        this.updateUserProfile(uid, challengeId, videoId);

        // 비디오 반응 조회
        videoDto.setReactions(videoReactionStore.selectReactionsByVideoId(videoId));
        // 비디오 유저 반응 조회
        videoDto.setUserReactions(videoReactionStore.findVideoReactionByVideoIdAndUid(videoId, uid));
        // 비디오 상태
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

        return videoDto;
    }

    @Override
    public Map<String, Object> registerVideoReaction(VideoReactionDto videoReactionDto) {
    	//Date start = new Date();
		if ( videoReactionStore.findVideoReactionByVideoIdAndUidAndReaction(videoReactionDto) != null ) {
		    //throw ExceptionUtil.createOnfBizException("ONF_0004");
		} else {
			videoReactionStore.saveVideoReaction(videoReactionDto);
		}
		//Date check = new Date();
        //Date endSave = new Date();
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("reactions", videoReactionStore.selectReactionsByVideoId(videoReactionDto.getVideoId()));
        rtnMap.put("userReactions", videoReactionStore.findVideoReactionByVideoIdAndUid(videoReactionDto.getVideoId(), videoReactionDto.getUid()));
        //Date endSelect = new Date();
		/*LOGGER.info("==== 체크 시간 : " + (check.getTime() - start.getTime()));
		LOGGER.info("==== 저장 시간 : " + (endSave.getTime() - check.getTime()));
		LOGGER.info("==== 로드 시간 : " + (endSelect.getTime() - endSave.getTime()));
		LOGGER.info("==== 총 수행 시간 : " + (endSelect.getTime() - start.getTime()));*/
        return rtnMap;
    }

    @Override
    public Map<String, Object> deleteVideoReaction(VideoReactionDto videoReactionDto) {
        videoReactionStore.deleteVideoReactionByVideoIdAndUidAndReaction(videoReactionDto);

        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("reactions", videoReactionStore.selectReactionsByVideoId(videoReactionDto.getVideoId()));
        rtnMap.put("userReactions", videoReactionStore.findVideoReactionByVideoIdAndUid(videoReactionDto.getVideoId(), videoReactionDto.getUid()));

        return rtnMap;
    }

    private void createVideoStatus(VideoDto videoDto, VideoStatusDto videoStatusDto, String uid) {
        if ( videoDto == null )
            throw ExceptionUtil.createOnfBizException("ONF_0001", "비디오");

        if (videoStatusDto == null) {
            // 비디오 최초 조회 : progress 등록
            videoStatusStore.saveVideoStatus(
                    VideoStatusDto.builder()
                            .status(false) // 비디오 시청 상태는 진행중(false)상태로 셋팅
                            .user(UserDto.builder().uid(uid).build())
                            .video(videoDto)
                            .created(LocalDateTime.now())
                            .updated(LocalDateTime.now())
                            .build());
        } else {
            // 비디오 조회 : progress 업데이트
            videoStatusStore.saveVideoStatus(
                    VideoStatusDto.builder()
                            .videoStatusId(videoStatusDto.getVideoStatusId())
                            .status(videoStatusDto.isStatus()) // 비디오 시청 상태는 기존 비디오 상태로 셋팅
                            .user(UserDto.builder().uid(uid).build())
                            .video(videoDto)
                            .created(LocalDateTime.now())
                            .updated(LocalDateTime.now())
                            .build());
        }

    }

    private void createChallengeStatus(String uid, String challengeId) {
        ChallengeStatusDto challengeStatusDto = challengeStatusStore.findChallengeStatusByChallengeIdAndUid(challengeId, uid);

        if (challengeStatusDto == null) {
            // 챌린지 최초 조회 : challengeStatus 등록
            challengeStatusStore.saveChallengeStatus(
                    ChallengeStatusDto.builder()
                            .user(UserDto.builder().uid(uid).build())
                            .challenge(ChallengeDto.builder().challengeId(challengeId).build())
                            .status(false) // 챌린지 상태는 진행중(false)상태로 셋팅
                            .build());
        } else {
            // 챌린지 조회 : challengeStatus 업데이트
            challengeStatusStore.saveChallengeStatus(
                    ChallengeStatusDto.builder()
                            .challengeStatusId(challengeStatusDto.getChallengeStatusId())
                            .user(UserDto.builder().uid(uid).build())
                            .challenge(ChallengeDto.builder().challengeId(challengeId).build())
                            .status(challengeStatusDto.isStatus()) // 챌린지 상태는 기존 비디오 상태로 셋팅
                            .build());

        }
    }

    private void updateUserProfile(String uid, String challengeId, String videoId) {
        UserProfileDto userProfileDto = userProfileStore.findUserProfileByUid(uid);

        if (userProfileDto == null)
            throw ExceptionUtil.createOnfBizException("ONF_0001", "사용자 프로필");


        userProfileStore.saveUserProfileAndUid(
                UserProfileDto.builder()
                        .userProfileId(userProfileDto.getUserProfileId())
                        .titleDto(userProfileDto.getTitleDto())
                        .challenge(ChallengeDto.builder().challengeId(challengeId).build())
                        .video(VideoDto.builder().videoId(videoId).build())
                        .build()
                ,
                uid
        );
    }

    private void updateVideoStatus(VideoStatusDto videoStatusDto) {
        // 최조 조회 아님 : 기존 progress 조회(해당 챌린지의 비디오) updated 수정
        videoStatusStore.saveVideoStatus(videoStatusDto);
    }
}
