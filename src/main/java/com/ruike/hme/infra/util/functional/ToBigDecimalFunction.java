package com.ruike.hme.infra.util.functional;

import java.math.BigDecimal;

/**
 * <p>
 * 转换BigDecimal函数式接口
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/9 10:25
 */
@FunctionalInterface
public interface ToBigDecimalFunction<T> {
    BigDecimal applyAsBigDecimal(T value);
}