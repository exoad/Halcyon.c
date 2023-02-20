package com.jackmeng.halcyon.core.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// simple marker annotation to represent that a potential function/method can return NULL
@Retention(RetentionPolicy.SOURCE) @Documented @Target(ElementType.METHOD)
/**
 * @author Jack Meng
 */
public @interface has_Nullable {

}
