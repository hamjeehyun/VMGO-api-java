package vmgo.store.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import vmgo.controller.UserController;
import vmgo.domain.dto.TitleDto;
import vmgo.domain.dto.UserDto;
import vmgo.domain.dto.UserTitleDto;
import vmgo.store.UserTitleStore;
import vmgo.store.entity.Title;
import vmgo.store.entity.User;
import vmgo.store.entity.UserTitle;
import vmgo.store.repository.UserTitleRepository;
import vmgo.util.JsonUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserTitleStoreLogic implements UserTitleStore {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserTitleRepository repository;

    public UserTitleStoreLogic(UserTitleRepository repository) {
        this.repository = repository;
    }


    @Override
    public String saveUserTitle(UserTitleDto userTitleDto) {
        UserTitle userTitle = new UserTitle(userTitleDto);

        userTitle.setUpdated(LocalDateTime.now());
        repository.save(userTitle);
        return userTitle.getUserTitleId();
    }

    @Override
    public boolean existsByTitleAndUser(String titleId, String uid) {
        return repository.existsByTitleAndUser(new Title(titleId), new User(uid));
    }

    @Override
    public List<TitleDto> findAllTitleByUser(UserDto userDto) {
        User user = new User(userDto);
        List<UserTitle> userTitleList = repository.findAllByUser(user);
        return userTitleList.stream().map(UserTitle::toTitleListDto).collect(Collectors.toList());
    }
}
