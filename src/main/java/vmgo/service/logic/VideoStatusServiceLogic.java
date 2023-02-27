package vmgo.service.logic;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vmgo.domain.common.CommonResponseDto;
import vmgo.domain.dto.ChallengeStatusDto;
import vmgo.domain.dto.TitleDto;
import vmgo.domain.dto.UserDto;
import vmgo.domain.dto.UserTitleDto;
import vmgo.domain.dto.VideoStatusDto;
import vmgo.service.VideoStatusService;
import vmgo.store.ChallengeStatusStore;
import vmgo.store.ChallengeVideoStore;
import vmgo.store.TitleStore;
import vmgo.store.UserTitleStore;
import vmgo.store.VideoStatusStore;
import vmgo.util.ExceptionUtil;

import java.util.Map;

@Service
public class VideoStatusServiceLogic implements VideoStatusService {
    private final static Logger LOGGER = LoggerFactory.getLogger(VideoStatusServiceLogic.class);

    private final VideoStatusStore videoStatusStore;
    private final TitleStore titleStore;
    private final UserTitleStore userTitleStore;
    private final ChallengeVideoStore challengeVideoStore;
    private final ChallengeStatusStore challengeStatusStore;

    public VideoStatusServiceLogic(VideoStatusStore videoStatusStore, TitleStore titleStore, UserTitleStore userTitleStore, ChallengeVideoStore challengeVideoStore, ChallengeStatusStore challengeStatusStore) {
        this.videoStatusStore = videoStatusStore;
        this.titleStore = titleStore;
        this.userTitleStore = userTitleStore;
        this.challengeVideoStore = challengeVideoStore;
        this.challengeStatusStore = challengeStatusStore;
    }

    @Override
    public CommonResponseDto updateVideoStatusComplete(String uid, String challengeId, String videoId) {
        // 비디오 기록 조회
        VideoStatusDto videoStatusDto = videoStatusStore.findVideoStatusByUidAndVideoId(uid, videoId);

        if (videoStatusDto == null)
            throw ExceptionUtil.createOnfBizException("ONF_0005");

        // 해당 비디오 완료 - watchingStatus true
        this.updateVideoProgressStatusComplete(videoStatusDto);
        int incompleteVideoCount = this.confirmChallengeCompletion(uid, challengeId);

        if (incompleteVideoCount == 0) {
            // 다 봤을 때 - 해당 챌린지 상태 변경(true)
            ChallengeStatusDto challengeStatusDto = challengeStatusStore.findChallengeStatusByChallengeIdAndUid(challengeId, uid);
            challengeStatusDto.setComplete();
            challengeStatusStore.saveChallengeStatus(challengeStatusDto);

            // 타이틀 획득
            TitleDto titleDto = titleStore.findTitleByChallengeId(challengeId);
            if (userTitleStore.existsByTitleAndUser(titleDto.getTitleId(), uid)) {
                // 이미 가지고 있는 타이틀 일 경우
                throw ExceptionUtil.createOnfBizException("ONF_0006");
            }
            userTitleStore.saveUserTitle(
                    UserTitleDto.builder()
                            .user(UserDto.builder().uid(uid).build())
                            .title(titleDto)
                            .build()
            );

            return new CommonResponseDto(String.valueOf(HttpStatus.SC_OK), titleDto.getTitleName());
        } else {
            // 아직 본 영상이 남아 있을 때
            return new CommonResponseDto(String.valueOf(HttpStatus.SC_OK), incompleteVideoCount);
        }
    }

    private int confirmChallengeCompletion(String uid, String challengeId) {
        // uid + status(true) : (챌린지에 속한) 사용자가 다 본 영상의 개수 구하기
		/*// 챌린지에 속한 비디오 아이디 목록 가져오기
		List<ChallengeVideoDto> challengeVideoDtoList = challengeVideoStore.findAllByChallengeId(challengeId);
		List<String> videoIdList = challengeVideoDtoList.stream().map(ChallengeVideoDto::getVideoId).collect(Collectors.toList());
		int videoCount = videoIdList.size();
		
		for (String videoId : videoIdList) {
		    // 사용자의 비디오 상태를 확인
		    boolean videoStatus = videoStatusStore.existsByUidAndVideoIdAndWatchingStatusIsTrue(uid, videoId);
		
		    if (videoStatus) videoCount--; // 아직 안본 영상 개수
		    else return videoCount;
		}
    	return 0;*/

        Map<String, Integer> map = challengeVideoStore.findAllVideoStatusInChallenge(uid, challengeId);

        int allVideo = map.get("allVideo");
        // allVideo가 0이다 == challengeId 가 잘못되어 조회값이 없다
        if ( allVideo == 0 ) throw ExceptionUtil.createOnfBizException("ONF_0001", "잘못된 challengeId");

        int endVideo = map.get("endVideo");

        return allVideo - endVideo;
    }

    private void updateVideoProgressStatusComplete(VideoStatusDto videoStatusDto) {
        videoStatusStore.saveVideoStatus(
                VideoStatusDto.builder()
                        .status(true)
                        .videoStatusId(videoStatusDto.getVideoStatusId())
                        .user(videoStatusDto.getUser())
                        .video(videoStatusDto.getVideo())
                        .created(videoStatusDto.getCreated())
                        .build());
    }

}
