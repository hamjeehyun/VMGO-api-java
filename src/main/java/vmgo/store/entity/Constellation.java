package vmgo.store.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vmgo.domain.dto.ConstellationDto;
import vmgo.store.entity.common.BaseTimeEntity;

/**
 * @packageName vmgo.store.entity
 * @fileName Constellation.java
 * @author ssing_world
 * @date 2022/07/28
 * @description 별자리 엔티티
 * ================================
 * DATE				AUTHOR			NOTE
 * 2022/07/28       ssing_world		최초생성
 * 2022/08/14		RUBY			필드수정
 * 2022/08/21		RUBY			생성자 추가 - descrption 해금전 / 해금후 분리
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Constellation extends BaseTimeEntity<ConstellationDto> {
    /** 별자리 관리용 ID (PK) */
    @Id @Column(name = "constellation_id", updatable = false, length = 100)
    private String constellationId;
    /** 별자리 이름 */
    private String title;
    /** 별자리 설명 */
    private String description;
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
    
    public Constellation(ConstellationDto constellationDto) {
    	super(constellationDto);
    	this.constellationId = constellationDto.getConstellationId();
    	this.title = constellationDto.getTitle();
    	this.description = constellationDto.loadingDescritpion();
    	this.level = constellationDto.getLevel();
    	this.step = constellationDto.getStep();
    	this.imagePath = constellationDto.getImagePath();
    	this.componentName = constellationDto.getComponentName();
    	this.active = constellationDto.isActive();
    }
    
    public Constellation(String constellationId) {
    	this.constellationId = constellationId;
    }
    
    public void setDescription(String descrption) {
    	this.description = descrption;
    }
    
    public void setDescrption(String descrptionBefore, String descrptionAfter) {
    	StringBuffer strBuf = new StringBuffer();
    	strBuf.append(descrptionBefore == null ? "" : descrptionBefore);
    	strBuf.append("@@");
    	strBuf.append(descrptionAfter == null ? "" : descrptionAfter);
    	this.description = strBuf.toString();
    }

    @Override
    public void update(ConstellationDto dto) {
        BeanUtils.copyProperties(dto, this);
    }

    @Override
    public ConstellationDto toDto() {
    	return new ConstellationDto(constellationId, title, description, level, step, imagePath, componentName, active);
    }
}
