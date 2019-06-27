package com.rjsoft.magina.component.query.jpa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({
        ElementType.METHOD,
        ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface JpaConstraint {

    String type() default "";

    /*代码*/
    int[] code() default -1;

    /*规则*/
    int rule() default -1;
}
