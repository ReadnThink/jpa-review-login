package com.hospital.login.service;

import com.hospital.login.entity.User;
import com.hospital.login.entity.dto.UserDto;
import com.hospital.login.entity.dto.UserJoinRequest;
import com.hospital.login.exception.ErrorCode;
import com.hospital.login.exception.HospitalReviewAppException;
import com.hospital.login.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserDto join(UserJoinRequest request) {
        //Id가 중복이면 에러처리
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user -> {
                    throw new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME,String.format(
                            "UserName:%s",request.getUserName()));
                });
        //회원가입 .save()
        //save를 하면 DB에서 자동으로 id값을 부여해준다.
        User saveUser = userRepository.save(request.toEntity());

        return UserDto.builder()
                .id(saveUser.getId())
                .userName(saveUser.getUserName())
                .password(saveUser.getPassword())
                .emailAddress(saveUser.getEmailAddress())
                .build();
    }
}
