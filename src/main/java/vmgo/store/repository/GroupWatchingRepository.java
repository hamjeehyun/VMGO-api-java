package vmgo.store.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vmgo.store.entity.GroupWatching;

/**
 * @packageName vmgo.store.repository
 * @fileName GroupWatchingRepository.java
 * @author RUBY
 * @date 2022/07/25
 * @description 단체관람 레파지토리
 * ================================
 * DATE				AUTHOR			NOTE
 * 2022/07/25 		 RUBY			최초생성
 * 2022/09/02		 RUBY			쿼리메서드 추가(스케쥴러관련)
 */
@Repository
public interface GroupWatchingRepository extends JpaRepository<GroupWatching, String> ,JpaSpecificationExecutor<GroupWatching> {
	
	@Query("SELECT g FROM GroupWatching g LEFT JOIN FETCH g.video ORDER BY g.id DESC")
	List<GroupWatching> findAllGroupWatching();
	
//	@Query("SELECT g FROM GroupWatching g LEFT JOIN FETCH g.video WHERE g.id = :id")
//	GroupWatching findGroupWatchingById(String id);
	
	/**
	 * @return List&ltGroupWatching&gt
	 * - endDate가 비어있는 단관 리스트를 반환
	 */
	List<GroupWatching> findByEndDateIsNull();
	
	/**
	 * active=true이고
	 * 종료시각이 넘겨받은시각(현재시각) 전인 단관 리스트를 반환한다
	 * @param LocalDateTime
	 * @return List&ltGroupWatching&gt
	 */
	List<GroupWatching> findAllByEndDateIsBeforeAndActiveTrue(LocalDateTime ldt);
}
