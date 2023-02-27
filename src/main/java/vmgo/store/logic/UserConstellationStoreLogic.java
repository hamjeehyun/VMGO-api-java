package vmgo.store.logic;

import org.springframework.stereotype.Repository;
import vmgo.domain.dto.UserConstellationDto;
import vmgo.store.UserConstellationStore;
import vmgo.store.entity.Constellation;
import vmgo.store.entity.User;
import vmgo.store.entity.UserConstellation;
import vmgo.store.repository.UserConstellationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserConstellationStoreLogic implements UserConstellationStore {
	private final UserConstellationRepository repository;

	public UserConstellationStoreLogic(UserConstellationRepository repository) {
		this.repository = repository;
	}

	@Override
	public String saveUserConstellation(UserConstellationDto userConstellationDto) {

		UserConstellation userConstellation = new UserConstellation(userConstellationDto);

		userConstellation.setUpdated(LocalDateTime.now());
		repository.save(userConstellation);

		return userConstellation.getUserConstellationId();
	}
	
	@Override
	public UserConstellation saveUserConstellation(UserConstellation userConstellation) {
		userConstellation.setUpdated(LocalDateTime.now());
		return repository.save(userConstellation);
	}
	
	@Override
	public UserConstellation saveUserConstellation(Constellation constellation, User user) {
		return saveUserConstellation(new UserConstellation(constellation, user));
	}
	
	@Override
	public List<UserConstellationDto> saveAllUserConstellation(List<UserConstellationDto> userConstellationDtos) {

		List<UserConstellation> userConstellationList = userConstellationDtos.stream()
				.map(userConstellationDto -> {
					UserConstellation userConstellation = new UserConstellation(userConstellationDto);
					userConstellation.setUpdated(LocalDateTime.now());
					return userConstellation;
				}).collect(Collectors.toList());
		repository.saveAll(userConstellationList);
		return null;
//		return repository.saveAll(userConstellationList).stream().map(UserConstellation::toDto).collect(Collectors.toList());
	}

	@Override
	public UserConstellation findByConstellationAndUser(Constellation constellation, User user) {
		return repository.findByConstellationAndUser(constellation, user);
	}

	@Override
	public UserConstellation findByConstellationAndUser(UserConstellation userConstellation) {
		return repository.findByConstellationAndUser(userConstellation.getConstellation(), userConstellation.getUser());
	}
}
