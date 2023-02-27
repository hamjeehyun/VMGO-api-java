package vmgo.service;

import java.util.List;
import java.util.Map;

import vmgo.domain.dto.GroupWatchingDto;
import vmgo.store.entity.GroupWatching;

/**
 * @packageName vmgo.service
 * @fileName GroupWatchingService.java
 * @author RUBY
 * @date 2022/07/25
 * @description 단체관람 서비스 인터페이스
 * ================================
 * DATE				AUTHOR			NOTE
 * 2022/07/25 		 RUBY			최초생성
 * 2022/09/03		 RUBY			메서드 생성(스케쥴러 관련)
 */
public interface GroupWatchingService {
	
	/**
	 * @return Map&ltString,List&ltGroupWatchingDto&gt&gt<br/>
	 * - 전체 단관을 가져온다
	 */
	List<GroupWatchingDto> findAllGroupWatching();
	
	/**
	 * @return Map&ltString,List&ltGroupWatchingDto&gt&gt<br/>
	 * - 상영중인|상영예정인 단관 목록을 가져온다(분류당 최대 4건씩) 
	 */
	Map<String,Object> findAllGroupWatchingForUser();
	
	/**
	 * @param id
	 * @return GroupWatchingDto
	 * - 입력한 ID에 해당하는 단관Dto를 검색하여 반환
	 */
	GroupWatchingDto findGroupWatchingById(String id);
	
	/**
	 * 종료시간이 비어있는 단관을 조회하여<br>시작시간+비디오재생시간을 조합하여 종료시간을 업데이트함
	 * @return Map&ltString, int&gt<br>
	 * - {"totalLine" : 셀렉한 라인갯수}<br>
	 * - {"successLine" : 업데이트 성공한 라인갯수} 를 담아 반환 
	 */
	Map<String, Object> updateEndDate();

	/**
	 * GroupWatchingDto를 전달받아 insert 한다.
	 * @param groupWatchingDto
	 * @return GroupWatchingDto
	 * - 생성에 성공한 단관 레코드
	 */
	GroupWatchingDto insertGroupWatching(GroupWatchingDto groupWatchingDto);
	
	/**
	 * GroupWatchingDto를 전달받아 update 한다.
	 * @param groupWatchingDto
	 * @return GroupWatchingDto
	 * - 업데이트에 성공한 단관 레코드
	 */
	GroupWatchingDto updateGroupWatching(GroupWatchingDto groupWatchingDto);
	
	/**
	 * 프론트에서 전달받은 insert/update용 DTO를<br>
	 * GroupWatching 객체로 변환한다
	 * @param groupWatchingDto
	 * @return GroupWatching
	 */
	GroupWatching buildingGroupWatchingEntity(GroupWatchingDto groupWatchingDto);
	
	/**
	 * 종료시간에서 5분이 지나고 active가 true인 단관 리스트를 조회하고<br>
	 * 해당건이 존재할 시 active를 false로 수정한다.
	 */
	void updateEndedGroupWatching();
}
