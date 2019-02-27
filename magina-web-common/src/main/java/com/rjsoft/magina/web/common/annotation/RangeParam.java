package com.rjsoft.magina.web.common.annotation;

import java.lang.annotation.*;

@Target({ ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RangeParam {

    String value();

    Class<?> elementClass() default Object.class;

    String split() default " - ";
}
