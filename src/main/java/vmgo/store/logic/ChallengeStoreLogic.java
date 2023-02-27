package vmgo.store.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import vmgo.domain.dto.ChallengeDto;
import vmgo.store.ChallengeStore;
import vmgo.store.criteria.ChallengeSearchKeys;
import vmgo.store.criteria.ChallengeSearchSpecs;
import vmgo.store.entity.Challenge;
import vmgo.store.repository.ChallengeRepository;
import vmgo.util.JsonUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ChallengeStoreLogic implements ChallengeStore {
    private final static Logger LOGGER = LoggerFactory.getLogger(ChallengeStoreLogic.class);

    private final ChallengeRepository repository;

    public ChallengeStoreLogic(ChallengeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ChallengeDto> findAllChallenge(List<String> tags) {
        Map<ChallengeSearchKeys, Object> searchKeys = new HashMap<>();

        if (tags != null)
            searchKeys.put(ChallengeSearchKeys.TAGS, tags);

        List<Challenge> messages = repository.findAll(ChallengeSearchSpecs.searchWith(searchKeys));

        return messages.stream().map(Challenge::toListDto).collect(Collectors.toList());
    }

    @Override
    public List<ChallengeDto> findAllChallengeAndVideo(List<String> tags) {
        Map<ChallengeSearchKeys, Object> searchKeys = new HashMap<>();

        if (tags != null)
            searchKeys.put(ChallengeSearchKeys.TAGS, tags);

        List<Challenge> messages = repository.findAll(ChallengeSearchSpecs.searchWith(searchKeys));

        return messages.stream().map(Challenge::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ChallengeDto> findAllChallengeActiveIsTrue(List<String> tags) {
        Map<ChallengeSearchKeys, Object> searchKeys = new HashMap<>();

        searchKeys.put(ChallengeSearchKeys.ACTIVE, true);

        if (tags != null)
            searchKeys.put(ChallengeSearchKeys.TAGS, tags);

        List<Challenge> messages = repository.findAll(ChallengeSearchSpecs.searchWith(searchKeys));

        return messages.stream().map(Challenge::toListDto).collect(Collectors.toList());
    }

    @Override
    public List<ChallengeDto> findAllChallengeActiveIsTrueAndFeaturedIdTrue(List<String> tags) {
        Map<ChallengeSearchKeys, Object> searchKeys = new HashMap<>();

        searchKeys.put(ChallengeSearchKeys.FEATURED, true);
        searchKeys.put(ChallengeSearchKeys.ACTIVE, true);

        if (tags != null)
            searchKeys.put(ChallengeSearchKeys.TAGS, tags);

        List<Challenge> messages = repository.findAll(ChallengeSearchSpecs.searchWith(searchKeys));
        return messages.stream().map(Challenge::toListDto).collect(Collectors.toList());
    }

    @Override
    public ChallengeDto findChallengeById(String id) {
        Challenge challenge = repository.findById(id).orElse(null);

        if (challenge == null) {
            return null;
        }

        return challenge.toDto();
    }

    @Override
    public ChallengeDto findChallengeWithoutVideoById(String id) {
        Challenge challenge = repository.findById(id).orElse(null);

        if (challenge == null) {
            return null;
        }

        return challenge.toListDto();
    }

    @Override
    public String saveChallenge(ChallengeDto challengeDto) {
        Challenge challenge = new Challenge(challengeDto);

        challenge.setUpdated(LocalDateTime.now());
        repository.save(challenge);
        return challenge.getChallengeId();
    }

}
