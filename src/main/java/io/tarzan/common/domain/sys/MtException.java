package io.tarzan.common.domain.sys;

import io.choerodon.core.exception.CommonException;

public class MtException extends RuntimeException {

    private static final long serialVersionUID = 5402353342758315792L;
    private String code;

    public MtException() {
        super();
    }

    public MtException(String message) {
        super(message);
    }

    public MtException(String code, String message) {
        super(message);
        this.code = code;
        throw new CommonException(message);
    }

    public MtException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MtException(String message, Throwable cause) {
        super(message, cause);
    }

    public MtException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}

