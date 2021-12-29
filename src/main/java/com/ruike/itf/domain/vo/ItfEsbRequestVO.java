package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.*;
import java.util.*;

/**
 * @Classname ItfEsbRequestVO
 * @Description ESB 接口请求报文
 * @Date 2020/7/30 15:06
 * @Author yuchao.wang
 */
@ApiModel("ESB接口请求报文")
public class ItfEsbRequestVO implements Serializable {
    private static final long serialVersionUID = 1699064999635937233L;

    @ApiModelProperty("esbInfo")
    private ItfEsbRequestInfoVO esbInfo;

    @ApiModelProperty("requestInfo")
    private Map<String, Object> requestInfo;

    public ItfEsbRequestVO(){}

    public ItfEsbRequestVO(Date requestTime){
        this.setEsbInfo(new ItfEsbRequestInfoVO(requestTime));
    }

    public ItfEsbRequestInfoVO getEsbInfo() {
        return esbInfo;
    }

    public void setEsbInfo(ItfEsbRequestInfoVO esbInfo) {
        this.esbInfo = esbInfo;
    }

    public Map<String, Object> getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(Map<String, Object> requestInfo) {
        this.requestInfo = requestInfo;
    }

    @Override
    public String toString() {
        return "ItfEsbRequestVO{" +
                "esbInfo=" + esbInfo +
                ", requestInfo=" + requestInfo +
                '}';
    }
}