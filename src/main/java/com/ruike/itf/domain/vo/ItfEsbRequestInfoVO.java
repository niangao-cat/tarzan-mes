package com.ruike.itf.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.*;
import java.util.*;

/**
 * @Classname ItfEsbRequestInfoVO
 * @Description ESB 接口请求报文=esbinfo
 * @Date 2020/7/30 15:14
 * @Author yuchao.wang
 */
@ApiModel("ESB接口请求报文-esbinfo")
public class ItfEsbRequestInfoVO implements Serializable {
    private static final long serialVersionUID = 990521162104534319L;

    @ApiModelProperty("insId")
    private String instId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("requestTime")
    private Date requestTime;

    @ApiModelProperty("attr1")
    private String attr1;

    @ApiModelProperty("attr2")
    private String attr2;

    @ApiModelProperty("attr3")
    private String attr3;

    public ItfEsbRequestInfoVO(){}

    public ItfEsbRequestInfoVO(Date requestTime){
        this.setRequestTime(requestTime);
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public Date getRequestTime() {
        if (requestTime != null) {
            return (Date) requestTime.clone();
        } else {
            return null;
        }
    }

    public void setRequestTime(Date requestTime) {
        if (requestTime == null) {
            this.requestTime = null;
        } else {
            this.requestTime = (Date) requestTime.clone();
        }
    }

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public String getAttr3() {
        return attr3;
    }

    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    @Override
    public String toString() {
        return "ItfEsbInfoVO{" +
                "instId='" + instId + '\'' +
                ", requestTime=" + requestTime +
                ", attr1='" + attr1 + '\'' +
                ", attr2='" + attr2 + '\'' +
                ", attr3='" + attr3 + '\'' +
                '}';
    }
}