package vmgo.store;

import java.util.List;
import java.util.Map;

import vmgo.domain.dto.Reaction;
import vmgo.domain.dto.VideoReactionDto;
import vmgo.store.entity.VideoReaction;

/**
 * @packageName vmgo.store
 * @fileName VideoReactionStore.java
 * @author 씽씽
 * @date 2022/08/07
 * @description 비디오 리액션용 STORE<br>
 * ================================<br>
 * DATE				AUTHOR			NOTE<br>
 * 2022/08/07 		 씽씽			최초생성
 */
public interface VideoReactionStore {
    
	/**
     * 비디오 리액션을 저장한다
     * @param videoReactionDto
     */
    void saveVideoReaction(VideoReactionDto videoReactionDto);
    
    /**
     * 이미 등록된 리액션인지 확인한다
     * @param videoId
     * @param uid
     * @param reaction
     * @return VideoReaction
     */
    VideoReaction findVideoReactionByVideoIdAndUidAndReaction(VideoReactionDto videoReactionDto);
    
    /**
     * 단건의 비디오에대한 특정 유저의
     * 리액션을 조회한다
     * @param videoId
     * @param uid
     * @return VideoReactionDto
     */
    List<VideoReactionDto> findVideoReactionByVideoIdAndUid(String videoId, String uid);
    
    /**
     * 단건의 비디오에대한 전체 유저의
     * 리액션을 조회한다
     * @param videoId
     * @return
     */
    @Deprecated
    Map<Reaction, Object> findAllByVideoId(String videoId);
    
    /**
     * 유저가 해당 비디오에 남긴 특정 리액션을 삭제한다
     * @param videoReactionDto
     */
    void deleteVideoReactionByVideoIdAndUidAndReaction(VideoReactionDto videoReactionDto);
    
    /**
     * 단건의 비디오에 대한 전체 유저의 
     * 리액션을 조회한다
     * @param videoId
     * @return Map&ltString, Object&gt
     */
    Map<Reaction, Object> selectReactionsByVideoId(String videoId);
}
