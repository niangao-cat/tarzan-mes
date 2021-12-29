package com.ruike.itf.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @param
 * @author ywj
 * @version 0.0.1
 * @description 数据汇总数据显示
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/11
 * @time 17:29
 * @return
 */
@ApiModel("事务汇总接口表")
@Data
public class ItfObjectTransactionIfaceVO implements Serializable {

    private static final long serialVersionUID = -4642992154038252867L;

    @ApiModelProperty("接口表ID，主键")
    private String ifaceId;

    @ApiModelProperty(value = "接口汇总ID，此为调用ERP接口时传入的汇总ID")
    private String mergeId;

    @ApiModelProperty(value = "逻辑系统")
    private String system;

    @ApiModelProperty(value = "事务类型编码")
    private String transactionTypeCode;

    @ApiModelProperty(value = "移动类型")
    private String moveType;

    @ApiModelProperty(value = "移动原因")
    private String moveReason;

    @ApiModelProperty(value = "记账时间")
    private Date accountDate;

    @ApiModelProperty(value = "工厂编码（来源工厂）")
    private String plantCode;

    @ApiModelProperty(value = "事务时间")
    private Date transactionTime;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "事务汇总数量")
    private BigDecimal transactionQty;

    @ApiModelProperty(value = "事务批次")
    private String lotNumber;

    @ApiModelProperty(value = "事务单位")
    private String transactionUom;

    @ApiModelProperty(value = "仓库（来源仓库）")
    private String warehouseCode;

    @ApiModelProperty(value = "货位（来源货位）")
    private String locatorCode;

    @ApiModelProperty(value = "目标工厂")
    private String transferPlantCode;

    @ApiModelProperty(value = "目标仓库")
    private String transferWarehouseCode;

    @ApiModelProperty(value = "目标货位")
    private String transferLocatorCode;

    @ApiModelProperty(value = "成本中心编码")
    private String costcenterCode;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商地点编码")
    private String supplierSiteCode;

    @ApiModelProperty(value = "客户编码")
    private String customerCode;

    @ApiModelProperty(value = "客户地点编码")
    private String customerSiteCode;

    @ApiModelProperty(value = "来源单据类型")
    private String sourceDocType;

    @ApiModelProperty(value = "来源单据号")
    private String sourceDocNum;

    @ApiModelProperty(value = "来源单据行号")
    private String sourceDocLineNum;

    @ApiModelProperty(value = "工单号/内部订单号")
    private String workOrderNum;

    @ApiModelProperty(value = "工序")
    private String operationSequence;

    @ApiModelProperty(value = "预留/相关需求的编号")
    private String bomReserveNum;

    @ApiModelProperty(value = "预留/相关需求的项目编号")
    private String bomReserveLineNum;

    @ApiModelProperty(value = "完成标识")
    private String completeFlag;

    @ApiModelProperty(value = "生产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "接收批（山蒲业务字段）")
    private String deliveryBatch;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @ApiModelProperty(value = "容器类型编码")
    private String containerTypeCode;

    @ApiModelProperty(value = "销售订单号")
    private String soNum;

    @ApiModelProperty(value = "销售订单行号")
    private String soLineNum;

    @ApiModelProperty(value = "SN号")
    private String snNum;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "处理时间", required = true)
    private Date processDate;

    @ApiModelProperty(value = "处理消息")
    private String processMessage;

    @ApiModelProperty(value = "处理状态(N/P/E/S:正常/处理中/错误/成功)", required = true)
    private String processStatus;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    @ApiModelProperty(value = "内部订单号")
    private String insideOrder;

    @ApiModelProperty(value = "attributeCategory")
    private String attributeCategory;

    @ApiModelProperty(value = "物料凭证")
    private String attribute1;

    @ApiModelProperty(value = "凭证时间")
    private String attribute2;

    @ApiModelProperty(value = "attribute3")
    private String attribute3;

    @ApiModelProperty(value = "attribute4")
    private String attribute4;

    @ApiModelProperty(value = "attribute4")
    private String attribute5;

    @ApiModelProperty(value = "attribute6")
    private String attribute6;

    @ApiModelProperty(value = "attribute7")
    private String attribute7;

    @ApiModelProperty(value = "attribute8")
    private String attribute8;

    @ApiModelProperty(value = "attribute9")
    private String attribute9;

    @ApiModelProperty(value = "attribute10")
    private String attribute10;

    @ApiModelProperty(value = "attribute11")
    private String attribute11;

    @ApiModelProperty(value = "attribute12")
    private String attribute12;

    @ApiModelProperty(value = "attribute13")
    private String attribute13;

    @ApiModelProperty(value = "attribute14")
    private String attribute14;

    @ApiModelProperty(value = "attribute15")
    private String attribute15;

    @ApiModelProperty(value = "attribute16")
    private String attribute16;

    @ApiModelProperty(value = "attribute17")
    private String attribute17;

    @ApiModelProperty(value = "attribute18")
    private String attribute18;

    @ApiModelProperty(value = "attribute19")
    private String attribute19;

    @ApiModelProperty(value = "attribute20")
    private String attribute20;

    @ApiModelProperty(value = "attribute21")
    private String attribute21;

    @ApiModelProperty(value = "attribute22")
    private String attribute22;

    @ApiModelProperty(value = "attribute23")
    private String attribute23;

    @ApiModelProperty(value = "attribute24")
    private String attribute24;

    @ApiModelProperty(value = "attribute24")
    private String attribute25;

    @ApiModelProperty(value = "attribute26")
    private String attribute26;

    @ApiModelProperty(value = "attribute27")
    private String attribute27;

    @ApiModelProperty(value = "attribute28")
    private String attribute28;

    @ApiModelProperty(value = "attribute29")
    private String attribute29;

    @ApiModelProperty(value = "attribute30")
    private String attribute30;

    @ApiModelProperty(value = "目标销售订单行")
    private String transferSoLineNum;

    @ApiModelProperty(value = "目标批次")
    private String transferLotNumber;

    @ApiModelProperty(value = "特殊库存，销售订单库存")
    private String specStockFlag;

    @ApiModelProperty(value = "采购订单头")
    private String poNum;

    @ApiModelProperty(value = "目标销售订单")
    private String transferSoNum;

    @ApiModelProperty(value = "SAP移动事物分配代码")
    private String gmcode;

    @ApiModelProperty(value = "采购订单行")
    private String poLineNum;

    @ApiModelProperty(value = "冲销标识")
    private String backFlag;

    @ApiModelProperty(value = "SAP字段-VGE01、准备时间单位")
    private String lineAttribute1;

    @ApiModelProperty(value = "SAP字段-VGW01：准备时间")
    private String lineAttribute2;

    @ApiModelProperty(value = "SAP字段-VGE02：排产时间单位")
    private String lineAttribute3;

    @ApiModelProperty(value = "SAP字段-VGW02：排产时间")
    private String lineAttribute4;

    @ApiModelProperty(value = "SAP字段-VGE03：定额时间单位")
    private String lineAttribute5;

    @ApiModelProperty(value = "SAP字段-VGW03：定额时间")
    private String lineAttribute6;

    @ApiModelProperty(value = "SAP字段-VGE04：动能时间单位")
    private String lineAttribute7;

    @ApiModelProperty(value = "SAP字段-VGW04：动能时间")
    private String lineAttribute8;

    @ApiModelProperty(value = "SAP字段-VGE05：人工时间单位")
    private String lineAttribute9;

    @ApiModelProperty(value = "SAP字段-VGW05：人工时间")
    private String lineAttribute10;

    @ApiModelProperty(value = "lineAttribute11")
    private String lineAttribute11;

    @ApiModelProperty(value = "lineAttribute12")
    private String lineAttribute12;

    @ApiModelProperty(value = "创建人")
    private String createdByName;

    @ApiModelProperty(value = "更新人")
    private String lastUpdatedByName;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateDate;


}
