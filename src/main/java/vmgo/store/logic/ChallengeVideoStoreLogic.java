package vmgo.store.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import vmgo.domain.dto.ChallengeVideoDto;
import vmgo.domain.dto.VideoDto;
import vmgo.domain.interfaces.VideoStatusInChallengeInterface;
import vmgo.store.ChallengeVideoStore;
import vmgo.store.criteria.ChallengeVideoSearchKeys;
import vmgo.store.criteria.ChallengeVideoSearchSpecs;
import vmgo.store.entity.Challenge;
import vmgo.store.entity.ChallengeVideo;
import vmgo.store.repository.ChallengeVideoRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ChallengeVideoStoreLogic implements ChallengeVideoStore {
    private final static Logger LOGGER = LoggerFactory.getLogger(ChallengeVideoStore.class);

    private final ChallengeVideoRepository repository;

    public ChallengeVideoStoreLogic(ChallengeVideoRepository repository) {
        this.repository = repository;
    }

    @Override
    public String saveChallengeVideo(ChallengeVideoDto challengeVideoDto) {
        ChallengeVideo challengeVideo = new ChallengeVideo(challengeVideoDto);

        challengeVideo.setUpdated(LocalDateTime.now());
        repository.save(challengeVideo);
        return challengeVideo.getChallengeVideoId();
    }

    @Override
    public List<ChallengeVideoDto> findAllByChallengeId(String challengeId) {
        Map<ChallengeVideoSearchKeys, Object> searchKeys = new HashMap<>();

        if (challengeId != null)
            searchKeys.put(ChallengeVideoSearchKeys.CHALLENGE, new Challenge(challengeId));

        List<ChallengeVideo> messages = repository.findAll(ChallengeVideoSearchSpecs.searchWith(searchKeys));

        return messages.stream().map(ChallengeVideo::toDto).collect(Collectors.toList());
    }

    @Override
    public List<VideoDto> findAllVideoByChallengeId(String challengeId) {
        Map<ChallengeVideoSearchKeys, Object> searchKeys = new HashMap<>();

        if (challengeId != null)
            searchKeys.put(ChallengeVideoSearchKeys.CHALLENGE, new Challenge(challengeId));

        List<ChallengeVideo> messages = repository.findAll(ChallengeVideoSearchSpecs.searchWith(searchKeys));

        return messages.stream().map(ChallengeVideo::toVideoDto).collect(Collectors.toList());
    }

    @Override
    public int countByChallengeId(String challengeId) {
        return repository.countByChallenge(new Challenge(challengeId));
    }

    @Override
    public void deleteByChallengeId(String challengeId) {
        repository.deleteByChallenge(new Challenge(challengeId));
    }

	@Override
	public Map<String, Integer> findAllVideoStatusInChallenge(String uid, String challengeId) {
		
		Map<String, Integer> rtnMap = new HashMap<>();
		
		VideoStatusInChallengeInterface vsInChall = repository.findAllVideoStatusInChallenge(new Challenge(challengeId), uid);
		
		rtnMap.put("allVideo", vsInChall.getAllVideo() == null ? 0 : vsInChall.getAllVideo().intValue());
		rtnMap.put("endVideo", vsInChall.getEndVideo() == null ? 0 : vsInChall.getEndVideo().intValue());
		
		return rtnMap;
	}
}
