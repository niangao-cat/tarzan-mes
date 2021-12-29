package com.ruike.hme.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 入库明细查询报表  输入
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/04 11:35
 */
@Data
public class HmeStockInDetailsDTO implements Serializable {


    private static final long serialVersionUID = 412200742161271961L;

    @ApiModelProperty("站点")
    private String siteId;

    @ApiModelProperty("制造部")
    private String areaName;

    @ApiModelProperty("工单编号")
    private List<String> workOrderNumList;

    @ApiModelProperty("工单版本")
    private String productionVersion;

    @ApiModelProperty("物料编码")
    private List<String> materialCodeList;

    @ApiModelProperty("工单类型")
    private String workOrderType;

    @ApiModelProperty("工单状态")
    private String workOrderStatus;

    @ApiModelProperty("产线")
    private String prodLineId;

    @ApiModelProperty("入库率起")
    private BigDecimal rate;

    @ApiModelProperty("序列号")
    private List<String> materialLotCodeList;

    @ApiModelProperty("入库时间从")
    private String creationDateFrom;

    @ApiModelProperty("入库时间至")
    private String creationDateFromTo;

    @ApiModelProperty("入库容器")
    private List<String> containerCodeList;

    @ApiModelProperty("库存地点")
    private String locatorId;

    @ApiModelProperty("作业人")
    private String id;

    @ApiModelProperty("单据号")
    private String instructionDocNum;

    @ApiModelProperty("单据状态")
    private String instructionDocStatus;

}
