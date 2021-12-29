package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.List;

@Data
public class WmsProductionRequisitionMaterialExecutionDTO implements Serializable {
    private static final long serialVersionUID = -201777342842612075L;
    @ApiModelProperty(value = "tenantId")
    private  Long tenantId;
    @ApiModelProperty(value = "生产领料单Id")
    private String instructionDocId;
    @ApiModelProperty(value = "生产领料单")
    private String instructionDocNum;
    @ApiModelProperty(value = "工厂")
    private String siteId;
    @ApiModelProperty(value = "工厂编码")
    private String  siteCode;
    @ApiModelProperty(value = "工厂名称")
    private String siteName;
    @ApiModelProperty(value = "单据类型")
    @LovValue(value = "WX.WMS.WO_IO_DM_TYPE", meaningField = "instructionDocTypeMeaning")
    private String instructionDocType;
    @ApiModelProperty(value = "单据类型含义")
    private String instructionDocTypeMeaning;
    @ApiModelProperty(value = "工单")
    private String workOrderNum;
    @ApiModelProperty(value = "单据状态")
    @LovValue(value = "WX.WMS_C/R_DOC_STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;
    @ApiModelProperty(value = "单据状态含义")
    private String instructionDocStatusMeaning;
    @ApiModelProperty(value = "计划内/外")
    private String inOutPlan;
    @ApiModelProperty(value = "行数据")
    private List<WmsProductionRequisitionMaterialExecutionLineDTO> lineDTOList;






}
