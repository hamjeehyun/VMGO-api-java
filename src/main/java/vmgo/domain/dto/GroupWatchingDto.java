package vmgo.domain.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import vmgo.domain.common.BaseTimeDto;
import vmgo.store.entity.GroupWatching;
import vmgo.store.entity.Video;

/**
 * @packageName vmgo.domain.dto
 * @fileName GroupWatchingDto.java
 * @author RUBY
 * @date 2022/07/25
 * @description 단체관람 DTO
 * ================================
 * DATE				AUTHOR			NOTE
 * 2022/07/25 		 RUBY			최초생성
 */
@Data
@Getter
@Setter
public class GroupWatchingDto extends BaseTimeDto<GroupWatchingDto> {
	private String id;
	private String title;
	private String startDate;
	private String endDate;
	private String thumbnail;
	private String description;
	private boolean active;
	
	private VideoDto video;
	
	public GroupWatchingDto() {};
	
	@Builder
	public GroupWatchingDto(String id, String title, LocalDateTime startDate, LocalDateTime endDate, String thumbnail, String description,boolean active, Video video) {
		this.id = id;
		this.title = title;
		this.thumbnail = thumbnail;
		this.description = description;
		setStartDate(startDate);
		setEndDate(endDate);
		this.active = active;
		this.video = video.toDto();
	}
	
	public static GroupWatchingDto from(GroupWatching groupWatching) {
		return GroupWatchingDto.builder()
				.id(groupWatching.getId())
				.title(groupWatching.getTitle())
				.startDate(groupWatching.getStartDate())
				.endDate(groupWatching.getEndDate())
				.thumbnail(groupWatching.getThumbnail())
				.description(groupWatching.getDescription())
				.active(groupWatching.isActive())
				.video(groupWatching.getVideo())
				.build();
	}
	
	public void setStartDate(LocalDateTime startDate) {
		if ( startDate != null ) this.startDate = startDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
		else this.startDate = "";
	}
	
	public void setEndDate(LocalDateTime endDate) {
		/*
		 * endDate가 없으면 빈 string을 셋팅 
		 */
		if ( endDate != null ) this.endDate = endDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
		else this.endDate = "";
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	/*
	 * endDate가 비어있을떄 계산하여 출력하기 위한 setter
	 * 성능이 너무 느려 일단 주석처리
	public void setEndDate(LocalDateTime startDate, Video video) {
		long duration = Long.parseLong(video.getDuration());
		this.endDate = startDate.plusSeconds(duration).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
	}
	*/
}
