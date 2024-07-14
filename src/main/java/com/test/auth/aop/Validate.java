package com.test.auth.aop;

import com.test.auth.enums.LogicEnum;
import com.test.auth.enums.PermissionsEnum;
import com.test.auth.modal.Permissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Validate {
    PermissionsEnum[] permissions() default {};
    LogicEnum type () default LogicEnum.Any;
}
