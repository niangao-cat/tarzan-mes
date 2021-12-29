package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeServiceSplitRecordVO2
 * @Description HmeServiceSplitRecordVO2
 * @Date 2020/11/6 11:24
 * @Author yuchao.wang
 */
@Data
public class HmeServiceSplitRecordVO2 implements Serializable {
    private static final long serialVersionUID = -7399057498718617190L;
    
    @ApiModelProperty("主键id")
    private String splitRecordId;

    @ApiModelProperty(value = "SN编码")
    private String snNum;
    
    @ApiModelProperty(value = "顶层售后返品拆机主键ID")
    private String topSplitRecordId;

    @ApiModelProperty(value = "内部订单号")
    private String internalOrderNum;
    
    @ApiModelProperty(value = "生产指令ID")
    private String workOrderId;

    @ApiModelProperty(value = "创建时间")
    private Date creationDate;

    @ApiModelProperty(value = "SAP订单ID")
    private String sapOrderId;

    @ApiModelProperty(value = "SAP订单号")
    private String sapOrderNum;

    @ApiModelProperty(value = "单号")
    private String orderNum;

    @ApiModelProperty(value = "单号类型")
    private String orderNumType;
}