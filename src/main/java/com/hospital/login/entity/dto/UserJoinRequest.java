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

    public User toEntity() {
        return User.builder()
                .userName(this.getUserName())
                .password(this.password)
                .emailAddress(this.email)
                .build();
    }
}
