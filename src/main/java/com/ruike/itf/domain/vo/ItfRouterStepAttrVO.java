package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.*;

/**
 * @Classname ItfRouterStepAttrVO
 * @Description 报工事务VO
 * @Date 2020/8/20 14:07
 * @Author yuchao.wang
 */
@Data
public class ItfRouterStepAttrVO implements Serializable {
    private static final long serialVersionUID = 849293762964934839L;

    @ApiModelProperty(value = "工单号/内部订单号")
    private String workOrderNum;

    @ApiModelProperty(value = "工艺路线")
    private String routerName;

    @ApiModelProperty(value = "SAP工序")
    private String stepName;

    @ApiModelProperty(value = "工序")
    private String sequence;

    @ApiModelProperty(value = "需求数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "属性名")
    private String attrName;

    @ApiModelProperty(value = "属性值")
    private String attrValue;

}