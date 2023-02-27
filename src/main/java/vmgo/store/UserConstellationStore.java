package vmgo.store;

import vmgo.domain.dto.UserConstellationDto;
import vmgo.store.entity.Constellation;
import vmgo.store.entity.User;
import vmgo.store.entity.UserConstellation;

import java.util.List;

public interface UserConstellationStore {
	String saveUserConstellation(UserConstellationDto userConstellationDto);
	UserConstellation saveUserConstellation(UserConstellation userConstellation);
	UserConstellation saveUserConstellation(Constellation constellation, User user);
	List<UserConstellationDto> saveAllUserConstellation(List<UserConstellationDto> userConstellationDtos);
	UserConstellation findByConstellationAndUser(Constellation constellation, User user);
	UserConstellation findByConstellationAndUser(UserConstellation userConstellation);
}
