package com.rjsoft.magina.component.query.jpa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({
        ElementType.FIELD,
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface JpaComment {

    /*字段名称*/
    String name() default "";

    String alias() default "";

    JpaConstraint[] constraint() default {};

    JpaValue[] values() default {};

}
