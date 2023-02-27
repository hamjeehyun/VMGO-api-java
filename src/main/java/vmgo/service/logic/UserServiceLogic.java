package vmgo.service.logic;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import vmgo.domain.dto.ConstellationStatusDto;
import vmgo.domain.dto.TitleDto;
import vmgo.domain.dto.UserDto;
import vmgo.domain.dto.UserProfileDto;
import vmgo.domain.dto.UserRole;
import vmgo.service.ConstellationService;
import vmgo.service.UserService;
import vmgo.store.TitleStore;
import vmgo.store.UserProfileStore;
import vmgo.store.UserStore;
import vmgo.store.UserTitleStore;
import vmgo.util.ExceptionUtil;

@Service
public class UserServiceLogic implements UserService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceLogic.class);

    private final UserStore userStore;
    private final UserProfileStore userProfileStore;
    private final TitleStore titleStore;
    private final ConstellationService constellationService;
    private final UserTitleStore userTitleStore;


    public UserServiceLogic(UserStore userStore, UserProfileStore userProfileStore, TitleStore titleStore, ConstellationService constellationService, UserTitleStore userTitleStore) {
        this.userStore = userStore;
        this.userProfileStore = userProfileStore;
        this.titleStore = titleStore;
        this.constellationService = constellationService;
        this.userTitleStore = userTitleStore;
    }

    @Override
    public String registerUser(UserDto userDto) {
        if (userDto.getUid() != null){
            UserDto user = userStore.findUserByUid(userDto.getUid());
            if(user != null) {
                // 사용자가 존재 - 로그인
                return userStore.saveUser(
                        UserDto.builder()
                                .uid(userDto.getUid())
                                .region(userDto.getRegion()) // 접속 지역 - 변경
                                .nickName(userDto.getNickName()) // 사용자 닉네임 - 변경
                                .userId(userDto.getUserId()) // 사용자 아이디(트위터아이디) - 변경
                                .method(user.getMethod())
                                .userRole(user.getUserRole())
                                .build()
                );
            } else {
                // 사용자가 존재하지 않음 - 회원가입
                userDto.setUserRole(UserRole.USER);
                String uid = userStore.saveUser(userDto);
                userProfileStore.saveUserProfile((UserProfileDto) UserProfileDto.builder().uid(uid).build());
                return userStore.saveUser(userDto);
            }
        }
        return null;
    }

    @Override
    public UserProfileDto registerUserAndFindUserProfile(UserDto userDto) {
        // 로그인 과정
        String uid = this.registerUser(userDto);
        return this.findUserProfileByUid(uid);
    }

    @Override
    public UserDto findUserByUid(String id) {
        return userStore.findUserByUid(id);
    }

    @Override
    public UserProfileDto findUserProfileByUid(String uid) {
        // 사용자 프로필 조회
        UserProfileDto userProfileDto = userProfileStore.findUserProfileByUid(uid);
        if (userProfileDto == null)
            throw ExceptionUtil.createOnfBizException("ONF_0001", "사용자 프로필");

        // 사용자 정보 조회
        UserDto userDto = this.findUserByUid(uid);

        // 사용자가 가진 타이틀 조회
        List<TitleDto> titleDtoList = userTitleStore.findAllTitleByUser(userDto);

        // 사용자가 가진 별자리 조회
        ConstellationStatusDto constellationStatusDto = constellationService.findConstellationByUid(uid);

        // 사용자 현재 장착한 타이틀 조회
        TitleDto titleDto = new TitleDto();
        if (userProfileDto.getTitleDto().getTitleId() != null) {
            titleDto = titleStore.findTitleById(userProfileDto.getTitleDto().getTitleId());
        }

        userProfileDto.setUserProfile(titleDtoList, constellationStatusDto);
        userProfileDto.setUser(userDto);
        userProfileDto.setTitleDto(titleDto);

        return userProfileDto;
    }

    @Override
    public void updateUserTitle(String uid, String titleId) {
        // 사용자 프로필 조회
        UserProfileDto userProfileDto = userProfileStore.findUserProfileByUid(uid);

        // 타이틀 셋팅
        userProfileDto.setTitleDto(TitleDto.builder().titleId(titleId).build());

        // 사용자 프로필 업데이트
        userProfileStore.saveUserProfileAndUid(userProfileDto, uid);
    }
}
