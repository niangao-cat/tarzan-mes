package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

/***
 *@Description 接口数据查询字段
 *@Author ywj
 *@email wenjie.yang01@hand-china.com
 *@Date 2020/11/11
 *@Time 16:20
 *@Version 0.0.1
 */
@Data
public class ItfObjectTransactionResultQueryDTO {

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "事务批次")
    private String lotNumber;

    @ApiModelProperty(value = "移动类型")
    private String moveType;

    @ApiModelProperty(value = "仓库（来源仓库）")
    private String warehouseCode;

    @ApiModelProperty(value = "货位（来源货位）")
    private String locatorCode;

    @ApiModelProperty(value = "工单号/内部订单号")
    private String workOrderNum;

    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)")
    private String processStatus;

    @ApiModelProperty(value = "事务开始时间", required = true)
    private String transactionDateStart;

    @ApiModelProperty(value = "事务结束时间", required = true)
    private String transactionDateEnd;

    @ApiModelProperty(value = "接口汇总Id")
    private String mergeId;

}
