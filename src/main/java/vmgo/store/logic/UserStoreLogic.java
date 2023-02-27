package vmgo.store.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import vmgo.controller.UserController;
import vmgo.domain.dto.UserDto;
import vmgo.store.UserStore;
import vmgo.store.entity.User;
import vmgo.store.repository.UserRepository;
import vmgo.util.JsonUtil;

import java.time.LocalDateTime;

@Repository
public class UserStoreLogic implements UserStore {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserRepository repository;

    public UserStoreLogic(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public String saveUser(UserDto userDto) {
        User user = new User(userDto);

        user.setUpdated(LocalDateTime.now());
        repository.save(user);
        return user.getUid();
    }

    @Override
    public UserDto findUserByUid(String id) {
        User user = repository.findByUid(id);

        if (user == null) {
            return null;
        }

        return user.toDto();
    }
}
