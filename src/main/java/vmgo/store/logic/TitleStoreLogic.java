package vmgo.store.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import vmgo.domain.dto.TitleDto;
import vmgo.store.TitleStore;
import vmgo.store.entity.Title;
import vmgo.store.repository.TitleRepository;
import vmgo.util.JsonUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TitleStoreLogic implements TitleStore {
    private final static Logger LOGGER = LoggerFactory.getLogger(TitleStoreLogic.class);

    private final TitleRepository repository;

    public TitleStoreLogic(TitleRepository repository) {
        this.repository = repository;
    }

    @Override
    public String saveTitle(TitleDto titleDto) {
        Title title = new Title(titleDto);

        title.setUpdated(LocalDateTime.now());
        repository.save(title);
        return title.getTitleId();
    }

    @Override
    public List<TitleDto> findTitleAllByUid(String uid) {
        return null;
//        List<Title> titleList = repository.findAllById(uid);
//        return titleList.stream().map(Title::toDto).collect(Collectors.toList());
    }

    @Override
    public TitleDto findTitleById(String id) {
        Title title = repository.findById(id).orElse(null);
        return title.toDto();
    }

    @Override
    public TitleDto findTitleByChallengeId(String challengeId) {
        Title title = repository.findTitleByChallengeId(challengeId);

        if (title == null) {
            return null;
        }

        return title.toDto();
    }
}
