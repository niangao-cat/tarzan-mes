package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 入库明细查询报表 返回
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/04 10:55
 */
@Data
public class HmeStockInDetailsVO implements Serializable {

    private static final long serialVersionUID = 2322193349254542146L;

    @ApiModelProperty("站点")
    private String siteCode;

    @ApiModelProperty("制造部")
    private String attrValue;

    @ApiModelProperty("工单编号")
    private String workOrderNum;

    @ApiModelProperty("工单版本")
    private String productionVersion;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料描述")
    private String materialName;

    @ApiModelProperty("工单类型")
    private String workOrderType;

    @ApiModelProperty("工单状态")
    private String workOrderStatus;

    @ApiModelProperty("生产线")
    private String prodLineName;

    @ApiModelProperty("工单数量")
    private BigDecimal qty;

    @ApiModelProperty("已入库数量")
    private BigDecimal actualQty;

    @ApiModelProperty("已入库数量总数")
    private BigDecimal actualQtySum;

    @ApiModelProperty("入库率")
    private String rate;

    @ApiModelProperty("序列号")
    private String materialLotCode;

    @ApiModelProperty("入库时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @ApiModelProperty("入库容器")
    private String containerCode;

    @ApiModelProperty("库存地点")
    private String locatorCode;

    @ApiModelProperty("作业人")
    private String realName;

    @ApiModelProperty("单据号")
    private String instructionDocNum;

    @ApiModelProperty("单据状态")
    @LovValue(lovCode = "HME.INSTRUCTION_DOC_STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;

    @ApiModelProperty("单据状态含义")
    private String instructionDocStatusMeaning;
}
