package io.tarzan.common.domain.sys;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 数据返回对象.
 *
 * @author njq.niu@hand-china.com
 */
public class ResponseData<T> implements Serializable {

    private static final long serialVersionUID = -638488203753911569L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T rows;

    private boolean success = true;

    public ResponseData() {}

    public ResponseData(boolean success) {
        setSuccess(success);
    }

    public ResponseData(T list) {
        this(true);
        setRows(list);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getRows() {
        return rows;
    }

    public void setRows(T rows) {
        this.rows = rows;
    }
}
