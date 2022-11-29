package com.hospital.login.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.login.entity.dto.UserDto;
import com.hospital.login.entity.dto.UserJoinRequest;
import com.hospital.login.exception.ErrorCode;
import com.hospital.login.exception.HospitalReviewAppException;
import com.hospital.login.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UserService userService;
    UserJoinRequest userJoinRequest = UserJoinRequest.builder()
            .userName("sol")
            .password("123")
            .email("aaa")
            .build();
    @Test
    @DisplayName("회원가입 잘 되는지")
    @WithMockUser
    void join_success() throws Exception {
        //service의 join함수를 사용하면 UserDto를 반환한다.
        when(userService.join(any())).thenReturn(mock(UserDto.class));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("회원가입 실패 (중복)")
    @WithMockUser
    void join_fail() throws Exception {

       when(userService.join(any())).thenThrow(
               new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME,""));
        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("로그인 실패 - id없음")
    @WithMockUser
    void login_fail1() throws Exception {
        String id = "2";
        String password = "2";

        when(userService.login(id, password)).thenThrow(new HospitalReviewAppException(ErrorCode.NOT_FOUND, ""));
        //무엇을 보내서 : id, pw
        //무엇을 받을까 : Not_found
        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());

    }
    @Test
    @DisplayName("로그인 실패 - password잘못 입력")
    @WithMockUser
    void login_fail2() {

    }
    @Test
    @DisplayName("로그인 성공")
    @WithMockUser
    void login_fail3() {

    }



}