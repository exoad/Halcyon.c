package com.jackmeng.halcyon.core.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE_USE, ElementType.FIELD})
public @interface has_Unsafe {
  abstract String reason() default "UnknownReason";
}
