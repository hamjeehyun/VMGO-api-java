package vmgo.store;

import java.util.List;
import java.util.Map;

import vmgo.domain.dto.ChallengeVideoDto;
import vmgo.domain.dto.VideoDto;

public interface ChallengeVideoStore {
    String saveChallengeVideo(ChallengeVideoDto challengeVideoDto);
    List<ChallengeVideoDto> findAllByChallengeId(String challengeId);

    /**
     * 챌린지에 속하는 비디오 목록 조회
     * orderBy videoID asc
     * @param challengeId
     * @return
     */
    List<VideoDto> findAllVideoByChallengeId(String challengeId);
    int countByChallengeId(String challengeId);
    void deleteByChallengeId(String challengeId);
    
    /**
     * 챌린지의 전체 비디오 갯수와<br>
     * 해당 챌린지 비디오중 유저가 시청완료한 갯수를 반환
     * @param uid
     * @param challengeId
     * @return Map&ltString, Integer&gt<br>
     * allVideo : 챌린지의 전체 비디오 갯수<br>
     * endVideo : 유저가 시청완료한 비디오 갯수
     */
    Map<String, Integer> findAllVideoStatusInChallenge(String uid, String challengeId);
}
