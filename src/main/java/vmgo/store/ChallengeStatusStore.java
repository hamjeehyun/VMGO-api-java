package vmgo.store;

import vmgo.domain.dto.ChallengeStatusDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ChallengeStatusStore {
    String saveChallengeStatus(ChallengeStatusDto challengeStatusDto);
    ChallengeStatusDto findChallengeStatusByChallengeIdAndUid(String challengeId, String uid);
    ChallengeStatusDto findChallengeStatusFirstByUidAndStatusOrderByUpdated(String uid, boolean status);
    ChallengeStatusDto findChallengeStatusFirstByUidAndUpdate(String uid, LocalDateTime updated);
    List<ChallengeStatusDto> findAllChallengeStatusByUid(String uid);
    List<ChallengeStatusDto> findAllChallengeStatusByUidAndStatusIsFalse(String uid);
    int countByChallengeId(String challengeId);
}
