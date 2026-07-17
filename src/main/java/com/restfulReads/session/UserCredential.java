package com.restfulReads.session;

import com.restfulReads.enums.UserType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredential {

    private String email;

    private String password;

    private UserType userType;
}