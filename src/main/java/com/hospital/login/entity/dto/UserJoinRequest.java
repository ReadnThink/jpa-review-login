package com.hospital.login.entity.dto;

import com.hospital.login.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@Builder
public class UserJoinRequest {
    private String userName;
    private String password;
    private String email;

    public User toEntity(String password) { //password를 받아서 넣는다.
        return User.builder()
                .userName(this.getUserName())
                .password(password)
                .emailAddress(this.email)
                .build();
    }
}
