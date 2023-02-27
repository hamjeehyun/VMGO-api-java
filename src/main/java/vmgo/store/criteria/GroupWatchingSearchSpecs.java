package vmgo.store.criteria;

import org.springframework.data.jpa.domain.Specification;

/**
 * <pre>
 * @packageName vmgo.store.criteria
 * @fileName GroupWatchingSearchSpecs.java
 * </pre>
 * @author RUBY
 * @date 2022/07/25
 * @description 단관 검색을 위하여 생성한<br>
 * Specification Class<br>
 * 검색용 enum객체또한 보유중<br>
 * ================================<br>
 * DATE				AUTHOR			NOTE<br>
 * 2022/07/25 		 RUBY			최초생성<br>
 */
public class GroupWatchingSearchSpecs {
	
	/**
	 * @description 단관 리스트 접속 시<br>
	 * 현재 상영중인 | 상영 대기중인 | <del>상영이 끝난</del><br> 
	 * 단관을 서치하기 위하여 생성한<br>
	 * 상태값과 쿼리를 저장한 enum
	 */
	public enum SearchKey {
		NOW("SELECT g FROM GroupWatching g LEFT JOIN FETCH g.video WHERE CURRENT_TIME BETWEEN g.startDate AND g.endDate AND g.active = true ORDER BY g.startDate ASC")
		,UPCOMING("SELECT g FROM GroupWatching g LEFT JOIN FETCH g.video WHERE g.startDate > CURRENT_TIME AND g.active = true ORDER BY g.startDate ASC")
		//,PAST("SELECT g FROM GroupWatching g LEFT JOIN FETCH g.video WHERE g.endDate < CURRENT_TIME ORDER BY g.endDate DESC")
		// 이후 지난 단관 필요할시 주석제거하여 사용
		;

		private String value;
		private SearchKey(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
	}

}
