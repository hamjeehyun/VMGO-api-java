package vmgo.domain.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import vmgo.domain.common.BaseTimeDto;

/**
 * @packageName vmgo.domain.dto
 * @fileName ConstellationDto.java
 * @author ssing_world
 * @date 2022/07/25
 * @description <br>
 * ================================<br>
 * DATE				AUTHOR			NOTE<br>
 * 2022/07/25 		ssing_world		최초생성
 * 2022/08/14		RUBY			필드추가
 * 2022/08/21		RUBY			필드추가 - descrption before/after 분리관리
 * 2022/08/28		RUBY			descrptionBefore 관련 오동작 수정
 */
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@ToString
public class ConstellationDto extends BaseTimeDto<ConstellationDto> {
    /** 별자리 ID (PK) */
    private String constellationId;
    /** 별자리 이름 */
    private String title;
    /** 별자리 설명 (획득전) */
    private String descriptionBefore;
    /** 별자리 설명 (획득후) */
    private String descriptionAfter;
    /** 별자리 레벨 */
    private int level;
    /** 별자리 총 스텝 */
    private int step;
    /** 별자리 이미지 링크 */
    private String imagePath;
    /** 별자리 컴포넌트 이름 */
    private String componentName;
    /** 별자리 사용여부 */
    private boolean active;

    public ConstellationDto(String constellationId, String title, String description, int level, int step, String imagePath, String componentName, boolean active){
    	this.constellationId = constellationId;
    	this.title = title;
    	moldingDescription(description);
    	this.level = level;
    	this.step = step;
    	this.imagePath = imagePath;
    	this.componentName = componentName;
    	this.active = active;
    }
    
    public ConstellationDto(String constellationId, String title, String description, int level, int step, String imagePath, String componentName, boolean active, LocalDateTime created, LocalDateTime updated){
        this.constellationId = constellationId;
        this.title = title;
        moldingDescription(description);
        this.level = level;
        this.step = step;
        this.imagePath = imagePath;
        this.componentName = componentName;
        this.active = active;
        this.created = created;
        this.updated = updated;
    }
    
    public ConstellationDto(String constellationId, String title, String descriptionBefore, String descriptionAfter,
    		int level, int step, String imagePath, String componentName, boolean active, LocalDateTime created, LocalDateTime updated) {
    	this.constellationId = constellationId;
    	this.title = title;
    	this.descriptionBefore = descriptionBefore;
    	this.descriptionAfter = descriptionAfter;
    	this.level = level;
    	this.step = step;
    	this.imagePath = imagePath;
    	this.componentName = componentName;
    	this.active = active;
    	this.created = created;
    	this.updated = updated;
    }
    
	public void setDescriptionAfter(String description) {
		moldingDescription(description);
	}
    
    public void moldingDescription(String description) {
    	if ( description.contains("@@") ) {
    		String[] desArr = description.split("@@");
    		this.descriptionBefore = desArr.length < 1 ? "" : desArr[0] == null ? "" : desArr[0];
			this.descriptionAfter = desArr.length < 2 ? "" : desArr[1] == null ? "" : desArr[1];
    	} else {
    		this.descriptionAfter = description;
    	}
    }
    
    public String loadingDescritpion() {
    	StringBuffer rtnStr = new StringBuffer();
    	rtnStr.append(descriptionBefore == null ? "" : descriptionBefore);
    	rtnStr.append("@@");
    	rtnStr.append(descriptionAfter == null ? "" : descriptionAfter);
    	return rtnStr.toString();
    }

}
