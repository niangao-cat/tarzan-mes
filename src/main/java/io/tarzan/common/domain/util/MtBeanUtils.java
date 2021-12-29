package io.tarzan.common.domain.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author sijing.lu@hand-china.com
 */
public class MtBeanUtils {
    private MtBeanUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * copy is null Properties
     * 
     * @param source
     * @param target
     */
    public static void copyIsNullProperties(Object source, Object target) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        // 这里需要获取到外键字段
        Field[] fs = actualEditable.getDeclaredFields();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            Method targetPdReadMethod = targetPd.getReadMethod();

            if (writeMethod != null && targetPdReadMethod != null) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());

                if (sourcePd != null) {
                    try {
                        Object val = targetPdReadMethod.invoke(target);
                        boolean checkString =
                                        targetPdReadMethod.getReturnType().isAssignableFrom(String.class)
                                                        && val != null && StringUtils.isEmpty(String.valueOf(val));
                        boolean checkForeignKey =
                                        targetPdReadMethod.getReturnType().isAssignableFrom(Long.class)
                                                        && val != null ;
                        if (val == null || checkString || checkForeignKey) {
                            Method sourceReadMethod = sourcePd.getReadMethod();
                            if (sourceReadMethod != null && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0],
                                            sourceReadMethod.getReturnType())) {

                                if (!Modifier.isPublic(sourceReadMethod.getDeclaringClass().getModifiers())) {
                                    ReflectionUtils.makeAccessible(sourceReadMethod);
                                }

                                Object value = sourceReadMethod.invoke(source);
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    ReflectionUtils.makeAccessible(writeMethod);
                                }
                                writeMethod.invoke(target, value);
                            }
                        }

                    } catch (Exception var1) {
                        throw new FatalBeanException(
                                        "Could not copy property '" + targetPd.getName() + "' from source to target",
                                        var1);
                    }
                }
            }

        }
    }
}
