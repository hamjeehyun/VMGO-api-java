package vmgo.domain.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import vmgo.service.GroupWatchingService;

/**
 * @packageName vmgo.domain.common
 * @fileName SchedulerApplication.java
 * @author RUBY
 * @date 2022/09/02
 * @description 스케쥴러 클래스<br>
 * ================================<br>
 * DATE				AUTHOR			NOTE<br>
 * 2022/09/02 		 RUBY			최초생성
 */
@EnableScheduling
@SpringBootApplication
public class SchedulerApplication {
	private final static Logger LOGGER = LoggerFactory.getLogger(SchedulerApplication.class);
	
	private GroupWatchingService groupWatchingService;
	
	public SchedulerApplication(GroupWatchingService groupWatchingService) {
		this.groupWatchingService = groupWatchingService;
	}

	/*
	 * CRON 표현식 = 초 분 시 일 월 요 (년)
	 * 현재 설정값  = 0  0  * * *  * (모든 월/일/시의 00분 00초마다 스케쥴러 실행 == 1시간마다 실행)
	 * */
	@Scheduled(cron="0 0 * * * *")
	public void updateEndedGroupWatchingActive() {
		LOGGER.info("======= 단관groupWatching 스케쥴러 실행 =======");
		groupWatchingService.updateEndedGroupWatching();
	}
}
