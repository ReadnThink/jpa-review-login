package com.hospital.login.service;

import com.hospital.login.entity.User;
import com.hospital.login.entity.dto.UserDto;
import com.hospital.login.entity.dto.UserJoinRequest;
import com.hospital.login.exception.ErrorCode;
import com.hospital.login.exception.HospitalReviewAppException;
import com.hospital.login.repository.UserRepository;
import com.hospital.login.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.token.secret}") //환경설정에서 설정 할 수있다.
    private String secretKey;
    private Long expireTimeMs = Long.valueOf(1000 * 60 * 60); // 1시간


    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
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
        User saveUser = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));

        return UserDto.builder()
                .id(saveUser.getId())
                .userName(saveUser.getUserName())
                .password(saveUser.getPassword())
                .emailAddress(saveUser.getEmailAddress())
                .build();
    }

    public String login(String userName, String password) {
        // userName있는지 여부확인
        // 없으면 NOT FOUND에러 발생
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.NOT_FOUND,
                        String.format("%s는 가입된 적이 없습니다.", userName)));
        // password일치 하는지 여부 확인
        // 입력받은 passwor와 user.getPassword를 비교한다.
        if(!encoder.matches(password, user.getPassword())){
            throw new HospitalReviewAppException(ErrorCode.INVALID_PASSWORD,
                    String.format("password가 잘못 입력 되었습니다.", userName));
        }
        // userName, password 확인중 예외 안났으면 Token발행

        return JwtUtil.createToken(userName, secretKey, expireTimeMs); //key를 절대 넣으면 안된다.(@value를 사용 - spring에서 지원한다.)
    }
}
