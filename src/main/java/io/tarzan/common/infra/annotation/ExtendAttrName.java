package io.tarzan.common.infra.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 拓展属性名称
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 01:46:17
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtendAttrName {
    String value() default "";
}
