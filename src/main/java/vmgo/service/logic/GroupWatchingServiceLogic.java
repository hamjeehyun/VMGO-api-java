package vmgo.service.logic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vmgo.config.ExceptionMessageProperty;
import vmgo.domain.common.OnfException;
import vmgo.domain.dto.GroupWatchingDto;
import vmgo.service.GroupWatchingService;
import vmgo.store.GroupWatchingStore;
import vmgo.store.criteria.GroupWatchingSearchSpecs;
import vmgo.store.entity.GroupWatching;
import vmgo.store.entity.Video;
import vmgo.util.ExceptionUtil;

/**
 * @packageName vmgo.service.logic
 * @fileName GroupWatchingServiceLogic.java
 * @author RUBY
 * @date 2022/07/25
 * @description 단체관람 서비스 구현 
 * ================================ 
 *    DATE         AUTHOR     NOTE
 * 2022/07/25       RUBY    최초생성
 * 2022/09/03		RUBY	메서드 작성(스케쥴러관련)
 */
@Service
public class GroupWatchingServiceLogic implements GroupWatchingService {
	private final static Logger LOGGER = LoggerFactory.getLogger(GroupWatchingServiceLogic.class);

	@Autowired
	private GroupWatchingStore groupWatchingStore;

	@Override
	public Map<String,Object> findAllGroupWatchingForUser() {

		Map<String, Object> rtnMap = new HashMap<>();

		for (GroupWatchingSearchSpecs.SearchKey key : GroupWatchingSearchSpecs.SearchKey.values() ) {
			List<GroupWatchingDto> gwList = groupWatchingStore.findAllGroupWatchingByQuery(key.getValue()).stream()
					.map(GroupWatching::toDto).collect(Collectors.toList());
			rtnMap.put(key.toString(), gwList);
		}
		
		return rtnMap;
	}
	
	@Override
	public List<GroupWatchingDto> findAllGroupWatching() {
		List<GroupWatchingDto> rtnList = groupWatchingStore.findAllGroupWatchingBy().stream()
											.map(GroupWatching::toDto).collect(Collectors.toList());
		return rtnList;
	}


	@Override
	public GroupWatchingDto findGroupWatchingById(String id) {
		GroupWatching groupWatching = groupWatchingStore.findById(id);
		if ( groupWatching == null ) throw ExceptionUtil.createOnfBizException("ONF_0001", "단체관람");
		return groupWatching.toDto();
	}

	public Map<String, Object> updateEndDate() {
		Map<String, Object> map = new HashMap<>();
		
		// endDate가 NULL 인 ROW 리스트 받아오기
		List<GroupWatching> edNullList = groupWatchingStore.findEndDateIsNull();
		
		// 반환할 객체에 업데이트 예정인 totalLine 갯수 세팅
		map.put("totalLine", edNullList.size());
		
		int successCount = 0;
		
		// List를 돌며 startDate+duration 을 계산하여 endDate update
		for (GroupWatching gv : edNullList) {
			LocalDateTime startDate = gv.getStartDate();
			long duration = gv.getVideo().getDuration();

			gv.setEndDate(startDate.plusSeconds(duration));
			if (groupWatchingStore.updateEndDate(gv) != null)
				successCount++;
		}
		
		// 반환할 객체에 업데이트 성공한 successLine 갯수 세팅
		map.put("successLine", successCount);
		return map;
	}

	@Override
	public GroupWatchingDto insertGroupWatching(GroupWatchingDto groupWatchingDto) {
		// 이미 저장된 정보가 update 되는 것 방지
		if ( groupWatchingStore.findById(groupWatchingDto.getId()) != null) {
			throw ExceptionUtil.createOnfBizException("ONF_0002", "단체관람"); 
		}
		
		GroupWatching groupWatching = buildingGroupWatchingEntity(groupWatchingDto);
		return groupWatchingStore.saveGroupWatching(groupWatching).toDto();
	}

	@Override
	public GroupWatchingDto updateGroupWatching(GroupWatchingDto groupWatchingDto) {
		// 전달받은 객체가 신규 ROW로 insert 되는 것 방지
		if ( groupWatchingStore.findById(groupWatchingDto.getId()) == null ) {
			throw ExceptionUtil.createOnfBizException("ONF_0003", "단체관람");
		}
		
		GroupWatching groupWatching = buildingGroupWatchingEntity(groupWatchingDto);
		return groupWatchingStore.saveGroupWatching(groupWatching).toDto();
	};
	
	@Override
	public GroupWatching buildingGroupWatchingEntity(GroupWatchingDto groupWatchingDto) {
		
		// 날짜포맷 생성하여 startDate parsing
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		LocalDateTime startDate = LocalDateTime.parse(groupWatchingDto.getStartDate(), format);
		
		// startDate + duration 조합하여 endDate 계산
		LocalDateTime endDate = startDate.plusSeconds(groupWatchingDto.getVideo().getDuration());
		
		// videoId 셋팅
		Video video = new Video();
		video.setVideoId(groupWatchingDto.getVideo().getVideoId());
		
		// 객체 빌드
		return GroupWatching.builder()
				.id(groupWatchingDto.getId())
				.title(groupWatchingDto.getTitle())
				.startDate(startDate)
				.endDate(endDate)
				.thumbnail(groupWatchingDto.getThumbnail())
				.description(groupWatchingDto.getDescription())
				.active(groupWatchingDto.isActive())
				.video(video)
				.build();
	}

	@Override
	public void updateEndedGroupWatching() {
		List<GroupWatching> gwList = groupWatchingStore.findAllByEndDateIsBeforeAndActiveTrue(LocalDateTime.now().minusMinutes(5));
		for( GroupWatching gw : gwList ) {
			gw.setActive(false);
			groupWatchingStore.saveGroupWatching(gw);
			LOGGER.info("======= 종료 단관 " + gw.getId() + " 상태값 false로 전환 =======");
		}
	}

}