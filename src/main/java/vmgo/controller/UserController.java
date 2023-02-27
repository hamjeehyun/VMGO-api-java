package vmgo.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vmgo.domain.dto.UserDto;
import vmgo.domain.dto.UserProfileDto;
import vmgo.service.UserService;

@RestController
@RequestMapping("vmgo/user")
public class UserController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 사용자 로그인
     * 최초 로그인 - 등록
     * 최초 로그인이 아님 - 조회
     * @param userDto
     * @return uid
     */
    @Deprecated
    @PostMapping
    public String registerUser(@RequestBody UserDto userDto) {
        return userService.registerUser(userDto);
    }

    /**
     * 사용자 로그인
     * 최초 로그인 - 등록
     * 최초 로그인이 아님 - 조회
     * @param userDto
     * @return UserProfile
     */
    @PostMapping("profile")
    @ApiOperation(value="사용자 로그인/회원가입", notes="user 와 userProfile 정보가 함께 들어감 - 파이어베이스에서 가져온 정보 중 수정된 것이 있으면 uid 기준으로 DB 업데이트(nickName, userId, region)")
    public UserProfileDto registerUserAndFindUserProfile(@RequestBody UserDto userDto) {
        return userService.registerUserAndFindUserProfile(userDto);
    }

    @Deprecated
    @GetMapping("{uid}")
    public UserDto findUserByUid(@PathVariable(name = "uid") String uid) {
        return userService.findUserByUid(uid);
    }

    @GetMapping("profile/{uid}")
    public UserProfileDto findUserProfileByUid(@PathVariable(name = "uid") String uid) {
        return userService.findUserProfileByUid(uid);
    }

    /**
     * 현재 장착 타이틀 수정
     * @return
     */
    @PutMapping("profile/{uid}/title/{titleId}")
    @ApiOperation(value="사용자 타이틀 수정(for User)", notes="")
    public void updateUserTitle(@PathVariable(name = "uid") String uid, @PathVariable(name = "titleId") String titleId) {
        userService.updateUserTitle(uid, titleId);
    }
}
