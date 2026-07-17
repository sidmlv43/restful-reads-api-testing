package com.restfulReads.session;

import com.restfulReads.enums.UserType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {

    private final String email;

    private final String token;

    private final UserType userType;

    private final String password;
}