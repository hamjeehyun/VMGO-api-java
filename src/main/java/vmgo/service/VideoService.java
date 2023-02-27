package vmgo.service;

import java.util.List;
import java.util.Map;

import vmgo.domain.dto.VideoDto;
import vmgo.domain.dto.VideoReactionDto;

public interface VideoService {
    List<VideoDto> findAllVideo();
    VideoDto findVideoById(String id);
    VideoDto findVideoByIdAndRegisterStatus(String videoId, String uid, String challengeId);
    Map<String, Object> registerVideoReaction(VideoReactionDto videoReactionDto);
    Map<String, Object> deleteVideoReaction(VideoReactionDto videoReactionDto);
}
