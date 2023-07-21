package com.estgames.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) //얘는 파라미터에서 꺼내올것임
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}
