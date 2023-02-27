package vmgo.store;

import vmgo.domain.dto.UserDto;

public interface UserStore {
    String saveUser(UserDto userDto);
    UserDto findUserByUid(String id);
}
