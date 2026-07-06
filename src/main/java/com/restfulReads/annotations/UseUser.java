package com.restfulReads.annotations;

import com.restfulReads.enums.UserType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface UseUser {

    UserType value();

}