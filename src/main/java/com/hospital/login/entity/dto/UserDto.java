package com.hospital.login.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserDto {
    private Long id;
    private String userName;
    private String password;
    private String emailAddress;
}