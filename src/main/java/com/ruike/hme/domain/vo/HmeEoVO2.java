package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.*;

/**
 * @Classname HmeEoVO2
 * @Description EO返回数据
 * @Date 2020/9/1 14:14
 * @Author yuchao.wang
 */
@Data
public class HmeEoVO2 implements Serializable {
    private static final long serialVersionUID = -1413767713972832529L;

    @ApiModelProperty(value = "EO ID")
    private String eoId;

    @ApiModelProperty(value = "EO编码")
    private String eoNum;

    @ApiModelProperty(value = "工艺路线步骤ID")
    private String routerStepId;

    @ApiModelProperty(value = "入口步骤")
    private String entryStepFlag;

    @ApiModelProperty(value = "执行作业在工艺路线步骤上的状态")
    private String status;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "工单编号")
    private String workOrderNum;
}
