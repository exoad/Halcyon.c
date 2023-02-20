package com.jackmeng.halcyon.core.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE) @Documented @Target({ ElementType.CONSTRUCTOR, ElementType.METHOD,
    ElementType.PACKAGE }) public @interface has_ErrorCode {
  String code();

  String description();
}
