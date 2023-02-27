package vmgo.store;

import vmgo.domain.dto.TitleDto;

import java.util.List;

public interface TitleStore {
    String saveTitle(TitleDto titleDto);
    List<TitleDto> findTitleAllByUid(String uid);
    TitleDto findTitleById(String id);
    TitleDto findTitleByChallengeId(String challengeId);
}
