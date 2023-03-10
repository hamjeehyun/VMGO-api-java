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

        if (videoDto == null) throw ExceptionUtil.createOnfBizException("ONF_0001", "?????????");

        return videoDto;
    }

    @Override
    public VideoDto findVideoByIdAndRegisterStatus(String videoId, String uid, String challengeId) {
        VideoStatusDto videoStatusDto = videoStatusStore.findVideoStatusByUidAndVideoId(uid, videoId);
        // ????????? ??????
        VideoDto videoDto = this.findVideoById(videoId);

        // ????????? ?????? ????????????
        this.createVideoStatus(videoDto, videoStatusDto, uid);
        // ????????? ?????? ????????????
        this.createChallengeStatus(uid, challengeId);
        // ?????? ??? ?????????-????????? ????????????
        this.updateUserProfile(uid, challengeId, videoId);

        // ????????? ?????? ??????
        videoDto.setReactions(videoReactionStore.selectReactionsByVideoId(videoId));
        // ????????? ?????? ?????? ??????
        videoDto.setUserReactions(videoReactionStore.findVideoReactionByVideoIdAndUid(videoId, uid));
        // ????????? ??????
        if (videoStatusDto != null) {
            boolean videoStatus = videoStatusDto.isStatus();

            if (videoStatus) {
                // ????????? ?????? ??????
                videoDto.setUserStatusComplete();
            } else {
                // ????????? ?????????
                videoDto.setUserStatusWatching();
            }
        } else {
            // videoStatusDto ??? null??? ???????????? ????????? ??? ?????? ?????????
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
		/*LOGGER.info("==== ?????? ?????? : " + (check.getTime() - start.getTime()));
		LOGGER.info("==== ?????? ?????? : " + (endSave.getTime() - check.getTime()));
		LOGGER.info("==== ?????? ?????? : " + (endSelect.getTime() - endSave.getTime()));
		LOGGER.info("==== ??? ?????? ?????? : " + (endSelect.getTime() - start.getTime()));*/
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
            throw ExceptionUtil.createOnfBizException("ONF_0001", "?????????");

        if (videoStatusDto == null) {
            // ????????? ?????? ?????? : progress ??????
            videoStatusStore.saveVideoStatus(
                    VideoStatusDto.builder()
                            .status(false) // ????????? ?????? ????????? ?????????(false)????????? ??????
                            .user(UserDto.builder().uid(uid).build())
                            .video(videoDto)
                            .created(LocalDateTime.now())
                            .updated(LocalDateTime.now())
                            .build());
        } else {
            // ????????? ?????? : progress ????????????
            videoStatusStore.saveVideoStatus(
                    VideoStatusDto.builder()
                            .videoStatusId(videoStatusDto.getVideoStatusId())
                            .status(videoStatusDto.isStatus()) // ????????? ?????? ????????? ?????? ????????? ????????? ??????
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
            // ????????? ?????? ?????? : challengeStatus ??????
            challengeStatusStore.saveChallengeStatus(
                    ChallengeStatusDto.builder()
                            .user(UserDto.builder().uid(uid).build())
                            .challenge(ChallengeDto.builder().challengeId(challengeId).build())
                            .status(false) // ????????? ????????? ?????????(false)????????? ??????
                            .build());
        } else {
            // ????????? ?????? : challengeStatus ????????????
            challengeStatusStore.saveChallengeStatus(
                    ChallengeStatusDto.builder()
                            .challengeStatusId(challengeStatusDto.getChallengeStatusId())
                            .user(UserDto.builder().uid(uid).build())
                            .challenge(ChallengeDto.builder().challengeId(challengeId).build())
                            .status(challengeStatusDto.isStatus()) // ????????? ????????? ?????? ????????? ????????? ??????
                            .build());

        }
    }

    private void updateUserProfile(String uid, String challengeId, String videoId) {
        UserProfileDto userProfileDto = userProfileStore.findUserProfileByUid(uid);

        if (userProfileDto == null)
            throw ExceptionUtil.createOnfBizException("ONF_0001", "????????? ?????????");


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
        // ?????? ?????? ?????? : ?????? progress ??????(?????? ???????????? ?????????) updated ??????
        videoStatusStore.saveVideoStatus(videoStatusDto);
    }
}
