package vmgo.store;

import vmgo.domain.dto.ChallengeDto;

import java.util.List;

public interface ChallengeStore {
    List<ChallengeDto> findAllChallenge(List<String> tags);
    List<ChallengeDto> findAllChallengeAndVideo(List<String> tags);
    List<ChallengeDto> findAllChallengeActiveIsTrue(List<String> tags);
    List<ChallengeDto> findAllChallengeActiveIsTrueAndFeaturedIdTrue(List<String> tags);
    ChallengeDto findChallengeById(String id);
    ChallengeDto findChallengeWithoutVideoById(String id);
    String saveChallenge(ChallengeDto challengeDto);
}
