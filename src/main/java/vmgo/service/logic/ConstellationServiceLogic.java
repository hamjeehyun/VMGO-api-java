package vmgo.service.logic;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import vmgo.domain.dto.ConstellationDto;
import vmgo.domain.dto.ConstellationJoinDto;
import vmgo.domain.dto.ConstellationStatusDto;
import vmgo.domain.dto.UserConstellationDto;
import vmgo.domain.dto.UserDto;
import vmgo.domain.interfaces.UserConstellationInterface;
import vmgo.service.ConstellationService;
import vmgo.store.ConstellationStore;
import vmgo.store.UserConstellationStore;
import vmgo.store.VideoStatusStore;
import vmgo.store.entity.Constellation;
import vmgo.store.entity.User;
import vmgo.store.entity.UserConstellation;
import vmgo.util.ExceptionUtil;

/**
 * @packageName vmgo.service.logic
 *
 * @fileName ConstellationServiceLogic.java
 * @author RUBY
 * @date 2022/08/14
 * @description 별자리 서비스 구현<br>
 * ================================<br>
 * DATE				AUTHOR			NOTE<br>
 * 2022/08/14 		 RUBY			최초생성
 * 2022/08/20		 RUBY			유저 별자리 조회 / 유저 별자리 획득 로직 작성
 */
@Service
public class ConstellationServiceLogic implements ConstellationService {
	private final static Logger LOGGER = LoggerFactory.getLogger(ConstellationServiceLogic.class);

	private final ConstellationStore constellationStore;
	private final VideoStatusStore videoStatusStore;
	private final UserConstellationStore userConstellationStore;

	public ConstellationServiceLogic(ConstellationStore constellationStore, VideoStatusStore videoStatusStore, UserConstellationStore userConstellationStore) {
		this.constellationStore = constellationStore;
		this.videoStatusStore = videoStatusStore;
		this.userConstellationStore = userConstellationStore;
	}

	@Override
	public List<ConstellationDto> findAllConstellation() {
		return constellationStore.findAllConstellation().stream()
				.map(Constellation::toDto).collect(Collectors.toList());
	}

	@Override
	public ConstellationDto findConstellationById(String id) {
		Constellation constellation = constellationStore.findConstellationById(id);
		if ( constellation == null ) throw ExceptionUtil.createOnfBizException("ONF_0001", "별자리");
		return constellation.toDto();
	}

	@Override
	public ConstellationDto insertConstellation(ConstellationDto constellationDto) {
		// 이미 저장된 정보가 update 되는 것 방지
		if ( constellationStore.findConstellationById(constellationDto.getConstellationId()) != null ) {
			throw ExceptionUtil.createOnfBizException("ONF_0002", "별자리");
		}
		return constellationStore.saveConstellation(constellationDto).toDto();
	}

	@Override
	public ConstellationDto updateConstellation(ConstellationDto constellationDto) {
		// 전달받은 객체가 신규 ROW로 insert 되는 것 방지
		if ( constellationStore.findConstellationById(constellationDto.getConstellationId()) == null ) {
			throw ExceptionUtil.createOnfBizException("ONF_0003", "별자리");
		}
		return constellationStore.saveConstellation(constellationDto).toDto();
	}

	@Override
	public ConstellationStatusDto findConstellationByUid(String uid) {
		int videoCount = videoStatusStore.countByUidAndStatus(uid, true);
		List<ConstellationJoinDto.DBData> consList = constellationStore.findConstellationListByUser(uid).stream()
				.map(UserConstellationInterface::toDto).collect(Collectors.toList());
		
		int currentLevel = consList.stream()
				.filter(cons -> "ONGOING".equals(cons.getStatus()) || "LOCKED".equals(cons.getStatus()))
				.min(Comparator.comparing(ConstellationJoinDto.DBData::getConLevel))
				.orElse(new ConstellationJoinDto.DBData(-1))
				.getConLevel(); 
		
		int currentStep = videoCount -
				consList.stream()
				.filter(cons -> "COMPLETE".equals(cons.getStatus()) || "READY".equals(cons.getStatus()) )
				.map(ConstellationJoinDto.DBData::getConStep)
				.reduce(0, Integer::sum);
		
		List<ConstellationJoinDto.Response> rtnList = consList.stream().map(ConstellationJoinDto.Response::new)
				.collect(Collectors.toList());
		
		return ConstellationStatusDto.builder()
				.level(currentLevel)
				.step(currentStep)
				.list(rtnList)
				.build();
	}
	
	@Override
	public ConstellationStatusDto findConstellationByUidOld(String uid) {
		int videoCount = videoStatusStore.countByUidAndStatus(uid, true);
		List<UserConstellationInterface> consList = constellationStore.findConstellationListAndUserConstellStatus(uid);
		
		List<UserConstellationInterface> registCons = consList.stream()
				.filter(cons -> videoCount > cons.getTotalStep() && cons.getUserConId() == null )
				.collect(Collectors.toList());
		
		// user_constellation 에 없는 경우 등록
		if (registCons.size() != 0) {
			List<UserConstellationDto> userConstellationDtoList = registCons.stream()
					.map(registCon ->
					UserConstellationDto.builder()
					.user(UserDto.builder().uid(uid).build())
					.constellation(ConstellationDto.builder().constellationId(registCon.getConstellationId()).build())
					.build()
							).collect(Collectors.toList());
			userConstellationStore.saveAllUserConstellation(userConstellationDtoList);
			
			// 업데이트 된 결과를 다시 조회
			consList = constellationStore.findConstellationListAndUserConstellStatus(uid);
		}
		/*

		for ( UserConstellationInterface uc : registCons) {
			LOGGER.info("getConLevel " + uc.getConLevel());
			LOGGER.info("getConStep " + uc.getConStep());
			LOGGER.info("getImagePath " + uc.getImagePath());
			LOGGER.info("getUserConId " + uc.getUserConId());
			LOGGER.info("getConstellationId " + uc.getConstellationId());
			LOGGER.info("----------------------------------");
		}
		 */
		
		// 이하는 프론트로 나갈것들 이쁘게 뽑는 과정
		int currentLevel = consList.stream()
				.filter(cons -> cons.getUserConId() == null)
				.min(Comparator.comparing(UserConstellationInterface::getConLevel))
				.orElse(null)
				.getConLevel(); // 모든 별자리를 다 모았을때 어떻게 동작하는걸까?
		
		int currentStep = videoCount -
				consList.stream()
				.filter(cons -> cons.getUserConId() != null)
				.map(UserConstellationInterface::getConStep)
				.reduce(0, Integer::sum);
		
		List<String> currentImageLinks = consList.stream()
				.filter(cons -> cons.getUserConId() != null)
				.map(UserConstellationInterface::getImagePath)
				.collect(Collectors.toList());
		
		return ConstellationStatusDto.builder()
				.level(currentLevel)
				.step(currentStep)
				//.completeImg(currentImageLinks)
				.build();
	}

	@Override
	public UserConstellationDto insertUserConstellation(String constellationId, String uid) {
				
		// 이미 저장된 정보가 update 되는 것 방지
		UserConstellation userCons = new UserConstellation(new Constellation(constellationId), new User(uid));
		
		if ( userConstellationStore.findByConstellationAndUser(userCons) != null ) {
			throw ExceptionUtil.createOnfBizException("ONF_0002", "유저-별자리");
		}
		
		return userConstellationStore.saveUserConstellation(userCons).toDto();
	}
}
