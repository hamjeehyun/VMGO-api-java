package vmgo.store;

import java.time.LocalDateTime;
import java.util.List;

import vmgo.store.entity.GroupWatching;

/**
 * @packageName vmgo.store
 * @fileName GroupWatchingStore.java
 * @author RUBY
 * @date 2022/07/25
 * @description 단체관람 스토어
 * ================================
 * DATE				AUTHOR			NOTE
 * 2022/07/25 		 RUBY			최초생성
 * 2022/09/02		 RUBY			메서드 작성(스케쥴러 관련)
 */
public interface GroupWatchingStore {
	
	List<GroupWatching> findAllGroupWatchingBy();
	List<GroupWatching> findAllGroupWatchingByQuery(String query);
	
	/**
	 * endDate가 비어있는 단관 리스트를 반환
	 * @return List&ltGroupWatching&gt
	 */
	List<GroupWatching> findEndDateIsNull();
	
	/**
	 * endDate를 업데이트하고 성공시 GroupWatching반환
	 * @param 
	 * @return GroupWatching
	 */
	GroupWatching updateEndDate(GroupWatching groupWatching);
	
	/**
	 * 하나의 단관 ID로 해당하는 단관을 조회한다<br>
	 * 조회 실패시 NULL을 반환한다.
	 * @param id
	 * @return GroupWatching
	 */
	GroupWatching findById(String id);
	
	/**
	 * 전달받은 값을 저장한다<br>
	 * 저장 성공시 저장된 객체를 반환한다
	 * @param groupWatching
	 * @return GroupWatching
	 */
	GroupWatching saveGroupWatching(GroupWatching groupWatching);
	
	/**
	 * active=true이고
	 * 종료시각이 넘겨받은시각(현재시각) 전인 단관 리스트를 반환한다
	 * @param LocalDateTime
	 * @return List&ltGroupWatching&gt
	 */
	List<GroupWatching> findAllByEndDateIsBeforeAndActiveTrue(LocalDateTime ldt);
}
