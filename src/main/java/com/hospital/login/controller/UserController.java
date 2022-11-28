package com.hospital.login.controller;

import com.hospital.login.entity.Response;
import com.hospital.login.entity.dto.UserDto;
import com.hospital.login.entity.dto.UserJoinRequest;
import com.hospital.login.entity.dto.UserJoinResponse;
import com.hospital.login.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //회원가입
    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest) {
        log.info("name : {}   password : {}",userJoinRequest.getUserName(), userJoinRequest.getPassword());
        UserDto userDto = userService.join(userJoinRequest);
        return Response.success(new UserJoinResponse(userDto.getUserName(),userDto.getPassword(),userDto.getEmailAddress()));
    }
}
