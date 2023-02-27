package vmgo.store;

import vmgo.domain.dto.TitleDto;
import vmgo.domain.dto.UserDto;
import vmgo.domain.dto.UserTitleDto;

import java.util.List;

public interface UserTitleStore {
    String saveUserTitle(UserTitleDto userTitleDto);
    boolean existsByTitleAndUser(String titleId, String uid);
    List<TitleDto> findAllTitleByUser(UserDto userDto);
}
