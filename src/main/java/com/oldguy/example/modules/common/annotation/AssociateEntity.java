package com.oldguy.example.modules.common.annotation;

import java.lang.annotation.*;


/**
 * 	关联实体类
 * @author King
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AssociateEntity {

    /**
     *  前缀
     * @return
     */
    String pre() default "";

    String name() default "";
}
