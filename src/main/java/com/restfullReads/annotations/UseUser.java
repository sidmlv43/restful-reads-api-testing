package com.restfullReads.annotations;

import com.restfullReads.enums.UserType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface UseUser {

    UserType value();

}