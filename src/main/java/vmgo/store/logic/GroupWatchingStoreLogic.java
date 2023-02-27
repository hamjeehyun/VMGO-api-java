package vmgo.store.logic;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import vmgo.domain.dto.GroupWatchingDto;
import vmgo.service.logic.ChallengeServiceLogic;
import vmgo.store.GroupWatchingStore;
import vmgo.store.entity.GroupWatching;
import vmgo.store.repository.GroupWatchingRepository;

/**
 * @packageName vmgo.store.logic
 * @fileName GroupWatchingStoreLogic.java
 * @author RUBY
 * @date 2022/07/25
 * @description 단체관람 스토어 구현
 * ================================
 * DATE				AUTHOR			NOTE
 * 2022/07/25 		 RUBY			최초생성
 */
@Repository
public class GroupWatchingStoreLogic implements GroupWatchingStore{
	private final static Logger LOGGER = LoggerFactory.getLogger(GroupWatchingStoreLogic.class);
	
	@PersistenceContext 
    private EntityManager em;

	@Autowired
	private GroupWatchingRepository repository;
	
	@Override
	public List<GroupWatching> findAllGroupWatchingBy() {
		return repository.findAllGroupWatching();
	}
	
	@Override
	public List<GroupWatching> findAllGroupWatchingByQuery(String query) {
		return em.createQuery(query, GroupWatching.class).setFirstResult(0).setMaxResults(4).getResultList();
	}

	@Override
	public List<GroupWatching> findEndDateIsNull() {
		return repository.findByEndDateIsNull();
	}

	@Override
	public GroupWatching updateEndDate(GroupWatching groupWatching) {
		return repository.save(groupWatching);
	}

	@Override
	public GroupWatching findById(String id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public GroupWatching saveGroupWatching(GroupWatching groupWatching) {
		groupWatching.setUpdated(LocalDateTime.now());
		return repository.save(groupWatching);
	}

	@Override
	public List<GroupWatching> findAllByEndDateIsBeforeAndActiveTrue(LocalDateTime ldt) {
		return repository.findAllByEndDateIsBeforeAndActiveTrue(ldt);
	}

}
