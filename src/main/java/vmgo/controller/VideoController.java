package vmgo.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import vmgo.domain.common.CommonResponseDto;
import vmgo.domain.dto.Reaction;
import vmgo.domain.dto.ResponseDto;
import vmgo.domain.dto.VideoDto;
import vmgo.domain.dto.VideoReactionDto;
import vmgo.service.VideoService;
import vmgo.service.VideoStatusService;

@RestController
@RequestMapping("vmgo/video")
public class VideoController {
    private final static Logger LOGGER = LoggerFactory.getLogger(VideoController.class);

    private final VideoService videoService;
    private final VideoStatusService videoStatusService;

    public VideoController(VideoService videoService, VideoStatusService videoStatusService) {
        this.videoService = videoService;
        this.videoStatusService = videoStatusService;
    }

    @GetMapping("{videoId}")
    public VideoDto findVideoById(@PathVariable(name = "videoId") String videoId) {
        return videoService.findVideoById(videoId);
    }

    /**
     * 비디오 조회 시 video Status 생성 및 수정
     * @param videoId
     * @param uid
     * @param challengeId
     * @return
     */
    @GetMapping("{videoId}/{uid}/{challengeId}")
    @ApiOperation(value="비디오 조회 시 video Status 생성 및 수정", notes="비디오 조회 시 video Status 생성 및 수정")
    public VideoDto findVideoByIdAndRegisterProgress(@PathVariable(name="videoId") String videoId,
                                                     @PathVariable(name = "uid") String uid,
                                                     @PathVariable(name = "challengeId") String challengeId) {
        return videoService.findVideoByIdAndRegisterStatus(videoId, uid, challengeId);
    }

    /**
     * 비디오 시청완료 시 Progress 상태
     * @param videoId
     * @param uid
     * @param challengeId
     * @return
     */
    @PutMapping("{videoId}/{uid}/{challengeId}/complete")
    @ApiOperation(value="비디오 시청완료", notes="비디오 시청완료 시 Progress 상태 업데이트")
    public CommonResponseDto updateProgressStatusComplete(@PathVariable(name = "uid") String uid,
                                                          @PathVariable(name = "challengeId") String challengeId,
                                                          @PathVariable(name="videoId") String videoId) {
        return videoStatusService.updateVideoStatusComplete(uid, challengeId, videoId);
    }

    /**
     * 비디오 반응 추가
     * @param videoId
     * @param uid
     * @param reaction
     * @return 비디오반응 id(videoReactionId)
     */
    @PostMapping("{videoId}/reaction/{uid}/{reaction}")
    @ApiOperation(value="비디오 리액션 등록", notes="비디오 리액션 등록")
    public Map<String, Object> registerVideoReaction(@PathVariable(name="videoId") String videoId,
    											@PathVariable(name="uid") String uid,
    											@PathVariable(name="reaction") Reaction reaction) {
        return videoService.registerVideoReaction(new VideoReactionDto(videoId, uid, reaction));
    }
    
    /**
     * 비디오 반응 삭제
     */
    @DeleteMapping("{videoId}/reaction/{uid}/{reaction}")
    @ApiOperation(value="비디오 리액션 삭제", notes="비디오 리액션 삭제")
    public Map<String, Object> deleteVideoReaction(@PathVariable(name="videoId") String videoId,
									@PathVariable(name="uid") String uid,
									@PathVariable(name="reaction") Reaction reaction) {
    	return videoService.deleteVideoReaction(new VideoReactionDto(videoId, uid, reaction));
    }

}
