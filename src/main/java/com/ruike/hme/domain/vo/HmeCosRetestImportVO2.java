package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * COS复测导入-查询VO
 *
 * @author sanfeng.zhang@hand-china.com 2021/1/25 10:15
 */
@Data
public class HmeCosRetestImportVO2 implements Serializable {

    private static final long serialVersionUID = -6979131396290376921L;

    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value = "是否打印 Y-已打印 N-未打印")
    private String printFlag;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "盒号")
    private String foxNum;

    @ApiModelProperty(value = "来料条码")
    private String sourceBarcode;

    @ApiModelProperty(value = "导入人")
    private String createBy;

    @ApiModelProperty(value = "操作者")
    private String operator;
}
