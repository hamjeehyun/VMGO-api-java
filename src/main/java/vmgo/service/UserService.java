package vmgo.service;

import vmgo.domain.dto.UserDto;
import vmgo.domain.dto.UserProfileDto;

public interface UserService {
    String registerUser(UserDto userDto);
    UserProfileDto registerUserAndFindUserProfile(UserDto userDto);
    UserDto findUserByUid(String uid);
    UserProfileDto findUserProfileByUid(String uid);
    void updateUserTitle(String uid, String titleId);
}
