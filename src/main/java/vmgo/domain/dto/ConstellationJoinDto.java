package vmgo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @packageName vmgo.domain.dto
 * @fileName ConstellationJoinDto.java
 * @author RUBY
 * @date 2022/08/21
 * @description 프론트 반환용 유지-별자리 객체<br>
 * ================================<br>
 * DATE				AUTHOR			NOTE<br>
 * 2022/08/21 		 RUBY			최초생성
 */
public class ConstellationJoinDto {
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DBData{
		/** 별자리 상태 */
		private String status;
		/** 별자리 아이디 */
		private String constellationId;
		/** 별자리 레벨 */
		private Integer conLevel;
		/** 별자리 스텝 */
		private Integer conStep;
		/** 별자리 이름 */
		private String conTitle;
		/** 별자리 설명(획득전) */
		private String descriptionBefore;
		/** 별자리 설명(획득후) */
		private String descriptionAfter;
		/** 별자리 컴포넌트 이름 */
		private String conCompName;
		/** 별자리 이미지 경로 */
		private String imagePath;
		/** 유저-별자리 테이블 PK */
		private String userConId;
		/** 별자리 획득 필요량(누적) */
		private Integer totalStep;
		
		public DBData(int conLevel) {
			this.conLevel = conLevel;
		}
		
		public DBData(String status, String constellationId, Integer conLevel, Integer conStep,
				String conTitle, String description, String conCompName, String imagePath, 
				String userConId, Integer totalStep) {
			super();
			this.status = status;
			this.constellationId = constellationId;
			this.conLevel = conLevel;
			this.conStep = conStep;
			this.conTitle = conTitle;
			moldingDescription(description);
			this.conCompName = conCompName;
			this.imagePath = imagePath;
			this.userConId = userConId;
			this.totalStep = totalStep;
		}
		
		public void moldingDescription(String description) {
			if ( description.contains("@@") ) {
				String[] desArr = description.split("@@");
				this.descriptionBefore = desArr.length < 1 ? "" : desArr[0] == null ? "" : desArr[0];
				this.descriptionAfter = desArr.length < 2 ? "" : desArr[1] == null ? "" : desArr[1];
			} else {
				this.descriptionBefore = "";
				this.descriptionAfter = description;
			}
		}
		
		public Response toResponse() {
			return new Response(this);
		}
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {
		/** 별자리 상태 */
		private String status;
		/** 별자리 아이디 */
		private String constellationId;
		/** 별자리 레벨 */
		private Integer level;
		/** 별자리 스텝 */
		private Integer step;
		/** 별자리 이름 */
		private String title;
		/** 별자리 설명(획득전) */
		private String descriptionBefore;
		/** 별자리 설명(획득후) */
		private String descriptionAfter;
		/** 별자리 컴포넌트 이름 */
		private String componentName;
		/** 별자리 이미지 경로 */
		private String imagePath;
		
		public Response(DBData dto) {
			this.constellationId = dto.getConstellationId();
			this.level = dto.getConLevel();
			this.step = dto.getConStep();
			this.title = dto.getConTitle();
			this.descriptionBefore = dto.getDescriptionBefore();
			this.descriptionAfter = dto.getDescriptionAfter();
			this.componentName = dto.getConCompName();
			this.imagePath = dto.getImagePath();
			setStatus(dto.getStatus());
		}
		
		public void setStatus(String status) {
			if ( "ONGOING".equals(status) || "LOCKED".equals(status) ) {
			//if ( "COMPLETE".equals(status) || "READY".equals(status) ) {
				this.descriptionAfter = "";
				this.imagePath = null;
			}
			this.status = status;
		}
	}
}
