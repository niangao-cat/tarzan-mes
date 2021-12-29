package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.List;

@Data
public class WmsProductionRequisitionMaterialExecutionLineDTO implements Serializable {
    private static final long serialVersionUID = 8082967434132528751L;
    @ApiModelProperty(value = "单据头Id")
    private String instructionDocId;
    @ApiModelProperty(value = "单据编号")
    private String instructionDocNum;
    @ApiModelProperty(value = "站点Id")
    private String siteId;
    @ApiModelProperty(value = "单据头类型")
    private String instructionDocType;
    @ApiModelProperty(value = "单据行Id")
    private String instructionId;
    @ApiModelProperty(value = "物料Id")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "需求数量")
    private Double quantity;
    @ApiModelProperty(value = "执行数量")
    private Double actualQuantity;
    @ApiModelProperty(value = "当前条码数量")
    private Double nowBarCodeQuantity;
    /*    @ApiModelProperty(value = "实绩数量")
        private Double quantity;*/
    @ApiModelProperty(value = "目标仓库Id")
    private String locatorId;
    @ApiModelProperty(value = "目标仓库编码")
    private String locatorCode;
    @ApiModelProperty(value = "目标仓库名称")
    private String locatorName;
    @ApiModelProperty(value = "单位Id")
    private String uomId;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "单位名称")
    private String uomName;
    @ApiModelProperty(value = "单据行状态")
    @LovValue(value = "WX.WMS_C/R_DOC_LINE_STATUS", meaningField = "instructionStatusMeaning")
    private String instructionStatus;
    @ApiModelProperty(value = "单据行状态含义")
    private String instructionStatusMeaning;
    @ApiModelProperty(value = "单据状态")
    private String instructionType;
    @ApiModelProperty(value = "单据行")
    private String instructionNum;
    @ApiModelProperty(value = "销售订单")
    private String soNumSoLineNum;
    @ApiModelProperty(value = "销售订单")
    private String soNum;
    @ApiModelProperty(value = "行号")
    private String soLineNum;
    @ApiModelProperty(value = "推荐货位")
    private String recommendedLocatorCode;
    @ApiModelProperty(value = "条码")
    private String barCode;

    @ApiModelProperty(value = "条码类型")
    private String barCodeType;

    @ApiModelProperty(value = "解绑标识")
    private String unbundingFlag;

    @ApiModelProperty(value = "装载标识")
    private String unLoadFlag;


    @ApiModelProperty(value = "条码集合")
    private List<String> lotCodeList;

    @ApiModelProperty(value = "单据物料Id")
    private String barCodematerialId;
    @ApiModelProperty(value = "单据物料编码")
    private String barCodematerialCode;
    @ApiModelProperty(value = "单据物料描述")
    private String barCodematerialName;
    @ApiModelProperty(value = "单据物料版本")
    private String barCodematerialVersion;
    @ApiModelProperty(value = "单据需求数量")
    private Double barCodequantity;
    @ApiModelProperty(value = "销售订单")
    private String insertSoLineSoNum;
    @ApiModelProperty(value = "明细长度")
    private Integer size;

    @ApiModelProperty(value = "条码容器")
    private String materialContainerCode;
    @ApiModelProperty(value = "条码")
    private String materialLotCode;

    @ApiModelProperty(value = "行扩展字段")
    private String instructionLineNum;
    @ApiModelProperty(value = "行扩展字段")
    private String bomReserveNum;
    @ApiModelProperty(value = "行扩展字段")
    private String  bomReserveLineNum;
    @ApiModelProperty(value = "行扩展字段")
    private String   specStockFlag;

    @ApiModelProperty(value = "明细")
    private List<WmsProductionRequisitionMaterialExecutionDetailDTO> detailDTOList;

    @ApiModelProperty(value = "亮灯按钮标识")
    private String lightFlag;
    @ApiModelProperty(value = "任务号")
    private String taskNum;
    @ApiModelProperty(value = "标识符")
    private String lightStatus;
    @ApiModelProperty(value = "亮灯/关灯")
    private String taskStatus;
    @ApiModelProperty(value = "亮灯接口返回标识符")
    private String status;
    @ApiModelProperty(value = "错误信息")
    private String message;
    @ApiModelProperty(value = "行数据集合")
    private List<WmsProductionRequisitionMaterialExecutionLineDTO> docLineList;

}
