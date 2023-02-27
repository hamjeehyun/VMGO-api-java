package vmgo.store;

/**
 * @packageName vmgo.store
 * @fileName AdminStore.java
 * @author RUBY
 * @date 2022/07/29
 * @description 어드민 스토어 인터페이스<br>
 * seq_number 테이블등에 접근함<br>
 * ================================<br>
 * DATE				AUTHOR			NOTE<br>
 * 2022/07/29 		 RUBY			최초생성
 */
public interface AdminStore {

	/**
	 * @param tableName 채번할 테이블 이름
	 * @return 채번된 기본키
	 */
	String getGeneratedKey(String tableName);
}
