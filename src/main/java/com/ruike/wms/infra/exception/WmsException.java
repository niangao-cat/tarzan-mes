package com.ruike.wms.infra.exception;

import org.hzero.core.exception.CheckedException;

/**
 * @Classname WmsException
 * @Description 程序中需要抛出一定需要捕捉的异常时，可使用该异常类
 * @Date 2019/9/16 9:55
 * @Created by weihua.liao
 */
public class WmsException extends CheckedException {
    public WmsException(String message, Object... parameters) {
        super(message, parameters);
    }

    public WmsException(Throwable cause, Object... parameters) {
        super(cause, parameters);
    }
}
