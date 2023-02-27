package vmgo.store.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vmgo.domain.dto.GroupWatchingDto;
import vmgo.store.entity.common.BaseTimeEntity;

/**
 * @packageName vmgo.store.entity
 * @fileName GroupWatching.java
 * @author RUBY
 * @date 2022/07/25
 * @description 단체관람 엔티티
 * ================================
 * DATE				AUTHOR			NOTE
 * 2022/07/25 		 RUBY			최초생성
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "group_watching")
public class GroupWatching extends BaseTimeEntity<GroupWatchingDto> {
	/** 단관 관리용 id(PK) */
	@Id 
	@Column(name = "watching_id", updatable = false, length = 100)
	private String id;
	/** 단관 제목 */
	private String title;
	/** 단관 시작시간 */
	@Column(name = "start_date")
	private LocalDateTime startDate;
	/** 단관 종료시간 */
	@Column(name = "end_date")
	private LocalDateTime endDate;
	/** 단관 썸네일 링크 */
	private String thumbnail;
	/** 단관 설명 */
	private String description;
	/** 단관 노출여부 */
	private boolean active;
	
	/** 단관할 VLIVE 영상 */
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="video_id", nullable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private Video video;
	
	public GroupWatching(GroupWatchingDto groupWatchingDto) {
		super(groupWatchingDto);
	}

	@Override
	public void update(GroupWatchingDto dto) {
	}

	/**
	 * 단관 엔티티를 Dto로 변환
	 * @return GroupWatchingDto
	 */
	@Override
	public GroupWatchingDto toDto() {
		return GroupWatchingDto.builder()
				.id(id)
				.title(title)
				.startDate(startDate)
				.endDate(endDate)
				.thumbnail(thumbnail)
				.description(description)
				.active(active)
				.video(video)
				.build();
	}
}
