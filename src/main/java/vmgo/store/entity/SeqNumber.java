package vmgo.store.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * @packageName vmgo.store.entity
 * @fileName SeqNumber.java
 * @author RUBY
 * @date 2022/07/29
 * @description 채번 테이블<br>
 * ================================<br>
 * DATE				AUTHOR			NOTE<br>
 * 2022/07/29 		 RUBY			최초생성
 */
@NamedStoredProcedureQuery(
		name = "get_generatedKey",
		procedureName = "VMGO.GET_GENERATEDPK_PROC",
		parameters = {
				@StoredProcedureParameter(name="tableName", type=String.class, mode=ParameterMode.IN),
				@StoredProcedureParameter(name="genaratedKey", type=String.class, mode=ParameterMode.OUT)
		}
)
@Getter
@Setter
@Entity
@Table
public class SeqNumber {
	/** ID // 테이블 이름을 ID로 사용 */
	@Id @Column(name = "id", length = 100)
	private String id;
	/** 시퀀스 설명 */
	@Column(length = 100)
	private String description;
	/** 시퀀스번호 현재 값 */
	private int number;
	/** 시퀀스번호 String 변환 시 최대 길이 */
	private int length;
	/** 접두사 */
	@Column(length = 40)
	private String prefix;
	/** 접미사 */
	@Column(length = 40)
	private String suffix;
	/** 시퀀스번호 증가량 */
	private int incremental;
	/** 시퀀스번호 리셋 번호(시퀀스가 해당 숫자에 도달하면 1로 리셋) */
	private int resetNo;
	
}
