package com.github.foundation.es.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 *  * @Description: 文档 Id 注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {FIELD, METHOD, ANNOTATION_TYPE})
public @interface Id {
}
