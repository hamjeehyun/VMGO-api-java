package vmgo.store.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import vmgo.domain.dto.ChallengeStatusDto;
import vmgo.store.ChallengeStatusStore;
import vmgo.store.entity.Challenge;
import vmgo.store.entity.ChallengeStatus;
import vmgo.store.entity.User;
import vmgo.store.repository.ChallengeStatusRepository;
import vmgo.util.JsonUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ChallengeStatusStoreLogic implements ChallengeStatusStore {
    private final static Logger LOGGER = LoggerFactory.getLogger(ChallengeStatusStoreLogic.class);

    private final ChallengeStatusRepository repository;

    public ChallengeStatusStoreLogic(ChallengeStatusRepository repository) {
        this.repository = repository;

    }

    @Override
    public String saveChallengeStatus(ChallengeStatusDto challengeStatusDto) {
        ChallengeStatus challengeStatus = new ChallengeStatus(challengeStatusDto);
        challengeStatus.setUpdated(LocalDateTime.now());
        repository.save(challengeStatus);
        return challengeStatus.getChallengeStatusId();
    }

    @Override
    public ChallengeStatusDto findChallengeStatusByChallengeIdAndUid(String challengeId, String uid) {
        ChallengeStatus challengeStatus = repository.findByChallengeAndUser(new Challenge(challengeId), new User(uid));
        if (challengeStatus == null) {
            return null;
        }
        return challengeStatus.toDto();
    }

    @Override
    public ChallengeStatusDto findChallengeStatusFirstByUidAndStatusOrderByUpdated(String uid, boolean status) {
        ChallengeStatus challengeStatus = repository.findFirstByUserAndStatusOrderByUpdatedDesc(new User(uid), status);
        if(challengeStatus == null){
            return null;
        }
        return challengeStatus.toDto();
    }

    @Override
    public ChallengeStatusDto findChallengeStatusFirstByUidAndUpdate(String uid, LocalDateTime updated) {
        ChallengeStatus challengeStatus = repository.findByUserAndUpdated(new User(uid), updated);

        LOGGER.debug("challengeStatus====>{}", JsonUtil.toJson(challengeStatus));
        LOGGER.debug("updated====>" + updated);

        if(challengeStatus == null){
            return null;
        }
        return challengeStatus.toDto();
    }

    @Override
    public List<ChallengeStatusDto> findAllChallengeStatusByUid(String uid) {
        List<ChallengeStatus> challengeStatusList = repository.findAllByUser(new User(uid));
        return challengeStatusList.stream().map(ChallengeStatus::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ChallengeStatusDto> findAllChallengeStatusByUidAndStatusIsFalse(String uid) {
        List<ChallengeStatus> challengeStatusList = repository.findAllByUserAndStatusIsFalse(new User(uid));
        return challengeStatusList.stream().map(ChallengeStatus::toDto).collect(Collectors.toList());
    }

    @Override
    public int countByChallengeId(String challengeId) {
        return repository.countByChallenge(new Challenge(challengeId));
    }
}
