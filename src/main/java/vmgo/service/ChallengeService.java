package vmgo.service;

import vmgo.domain.dto.ChallengeDto;
import vmgo.domain.dto.ResponseDto;

import java.util.List;

public interface ChallengeService {
    ResponseDto registerChallengeAndChallengeVideo(ChallengeDto challengeDto);
    void startChallengeByUid(String challengeId, String uid);
    ResponseDto updateChallengeAndChallengeVideo(ChallengeDto challengeDto);
    List<ChallengeDto> findAllChallenge(List<String> tags);
    List<ChallengeDto> findAllChallengeActiveIsTrue(List<String> tags);
    List<ChallengeDto> findAllChallengeActiveIsTrueByUid(String uid, List<String> tags);
    List<ChallengeDto> findAllChallengeByUidForDashboard(String uid);
    List<ChallengeDto> findAllChallengeActiveIsTrueAndFeaturedIdTrue(List<String> tags);
    ChallengeDto findChallengeById(String id);
    ChallengeDto findChallengeByIdAndUid(String id, String uid);
    void updateChallengeDuration();
}
