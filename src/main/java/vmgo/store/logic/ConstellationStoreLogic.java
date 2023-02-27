package vmgo.store.logic;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import vmgo.domain.dto.ConstellationDto;
import vmgo.domain.interfaces.UserConstellationInterface;
import vmgo.store.ConstellationStore;
import vmgo.store.entity.Constellation;
import vmgo.store.entity.User;
import vmgo.store.repository.ConstellationRepository;

@Repository
public class ConstellationStoreLogic implements ConstellationStore {
    private final ConstellationRepository repository;

    public ConstellationStoreLogic(ConstellationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ConstellationDto> findConstellationByUid(String uid) {
        return null;
    }

	@Override
	public List<Constellation> findAllConstellation() {
		return repository.findAllConstellation();
	}

	@Override
	public Constellation findConstellationById(String id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Constellation saveConstellation(ConstellationDto constellationDto) {
		Constellation constellation = new Constellation(constellationDto);
		constellation.setUpdated(LocalDateTime.now());
		return repository.save(constellation);
	}

	@Override
	public List<UserConstellationInterface> findConstellationListAndUserConstellStatus(String uid) {
		return repository.findConstellationListAndUserConstellStatus(new User(uid));
	}

	@Override
	public List<UserConstellationInterface> findConstellationListByUser(String uid) {
		return repository.findConstellationListByUser(new User(uid));
	}
}
