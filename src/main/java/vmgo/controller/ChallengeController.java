package vmgo.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vmgo.domain.dto.ChallengeDto;
import vmgo.domain.dto.ResponseDto;
import vmgo.service.AdminService;
import vmgo.service.ChallengeService;

@RestController
@RequestMapping("vmgo/challenge")
public class ChallengeController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ChallengeController.class);

    private final ChallengeService challengeService;
    private final AdminService adminService;

    public ChallengeController(ChallengeService challengeService, AdminService adminService) {
        this.challengeService = challengeService;
        this.adminService = adminService;
    }


    /**
     * 챌린지 등록 및 챌린지 비디오 등록
     * 챌린지 기본 정보 + (비디오 아이디 + 비디오 시간)
     * @param challengeDto
     * @return
     */
    @PostMapping("video")
    public ResponseDto registerChallengeAndChallengeVideoForAdmin(@RequestBody ChallengeDto challengeDto) {
        return challengeService.registerChallengeAndChallengeVideo(challengeDto);
    }

    /**
     * 챌린지 시작
     * @param challengeId
     * @param uid
     * @return
     */
    @PostMapping("/{challengeId}/user/{uid}/start")
    public void startChallengeByUid(@PathVariable(name = "challengeId") String challengeId,
                                      @PathVariable(name = "uid") String uid) {
         challengeService.startChallengeByUid(challengeId, uid);
    }

    /**
     * 챌린지 수정
     * 챌린지 기본 정보 + 비디오 아이디
     * @param challengeDto
     * @return
     */
    @PutMapping("video")
    public ResponseDto updateChallengeAndChallengeVideoForAdmin(@RequestBody ChallengeDto challengeDto) {
        return challengeService.updateChallengeAndChallengeVideo(challengeDto);
    }

    /**
     * 모든 챌린지 목록 조회 active-true
     * @return
     */
    @GetMapping
    public List<ChallengeDto> findAllChallengeActiveIsTrue(@RequestParam(name="tag", required = false) List<String> tags) {
        return challengeService.findAllChallengeActiveIsTrue(tags);
    }

    /**
     * 사용자의 참여 여부를 포함한 전체 목록 조회 active-true
     * @param uid
     * @return
     */
    @GetMapping("user/{uid}")
    public List<ChallengeDto> findAllChallengeActiveIsTrueByUid(@PathVariable(name = "uid") String uid,
                                                                @RequestParam(name="tag", required = false) List<String> tags) {
        return challengeService.findAllChallengeActiveIsTrueByUid(uid, tags);
    }

    /**
     * 사용자의 참여 여부를 포함한 전체 목록 조회 active-true
     * 사용자 대시보드용
     * @param uid
     * @return
     */
    @GetMapping("user/{uid}/watching")
    public List<ChallengeDto> findAllChallengeByUidForDashboard(@PathVariable(name = "uid") String uid) {
        return challengeService.findAllChallengeByUidForDashboard(uid);
    }

    /**
     * 모든 챌린지 목록 조회 (관리자)
     * @return
     */
    @GetMapping("admin")
    public List<ChallengeDto> findAllChallengeAdmin(@RequestParam(name="tag", required = false) List<String> tags) {
        return challengeService.findAllChallenge(tags);
    }

    /**
     * 메인 노출 챌린지 목록 조회
     * featured : true
     * @return
     */
    @GetMapping("main")
    public List<ChallengeDto> findAllMainChallenge(@RequestParam(name="tag", required = false) List<String> tags) {
        return challengeService.findAllChallengeActiveIsTrueAndFeaturedIdTrue(tags);
    }

    /**
     * 챌린지 상세 조회
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ChallengeDto findChallengeById(@PathVariable(name="id") String id){
        return challengeService.findChallengeById(id);
    }

    /**
     * 챌린지 상세 조회
     * 사용자의 챌린지 진행상황(%) 정보 포함
     * @param id
     * @return
     */
    @GetMapping("{id}/user/{uid}")
    public ChallengeDto findChallengeByIdAndUid(@PathVariable(name="id") String id,
                                                @PathVariable(name="uid") String uid){
        return challengeService.findChallengeByIdAndUid(id, uid);
    }

    @PutMapping("duration")
    public void updateChallengeDuration() {
        challengeService.updateChallengeDuration();
    }
    
    @GetMapping("key")
    public Map<String, Object> generatedChallengeKey(){
    	return adminService.getGeneratedKey("CHALLENGE");
    }

}
