package vmgo.store;

import vmgo.domain.dto.UserProfileDto;

public interface UserProfileStore {
    String saveUserProfile(UserProfileDto userProfileDto);
    String saveUserProfileAndUid(UserProfileDto userProfileDto, String uid);
    UserProfileDto findUserProfileByUid(String uid);
}
