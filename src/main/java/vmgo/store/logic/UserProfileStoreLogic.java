package vmgo.store.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import vmgo.controller.UserController;
import vmgo.domain.dto.UserProfileDto;
import vmgo.store.UserProfileStore;
import vmgo.store.entity.UserProfile;
import vmgo.store.repository.UserProfileRepository;
import vmgo.util.JsonUtil;

import java.time.LocalDateTime;

@Repository
public class UserProfileStoreLogic implements UserProfileStore {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserProfileStoreLogic.class);

    private final UserProfileRepository repository;

    public UserProfileStoreLogic(UserProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public String saveUserProfile(UserProfileDto userProfileDto) {
        UserProfile userProfile = new UserProfile(userProfileDto);

        userProfile.setUpdated(LocalDateTime.now());
        repository.save(userProfile);
        return userProfile.getUserProfileId();
    }

    @Override
    public String saveUserProfileAndUid(UserProfileDto userProfileDto, String uid) {
        UserProfile userProfile = new UserProfile(userProfileDto);

        userProfile.setUpdated(LocalDateTime.now());
        userProfile.setUid(uid);
        repository.save(userProfile);
        return userProfile.getUserProfileId();
    }

    @Override
    public UserProfileDto findUserProfileByUid(String uid) {
        UserProfile userProfile = repository.findByUid(uid);

        if (userProfile == null) {
            return null;
        }

        return userProfile.toDto();
    }
}
