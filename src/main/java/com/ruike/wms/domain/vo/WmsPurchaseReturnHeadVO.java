package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sanfeng.zhang@hand-china.com 2020/11/9 14:36
 */
@Data
public class WmsPurchaseReturnHeadVO implements Serializable {

    private static final long serialVersionUID = -8037032563242934039L;

    @ApiModelProperty(value = "单据id")
    private String instructionDocId;

    @ApiModelProperty(value = "单据类型")
    @LovValue(lovCode = "WMS.PO.TYPE", meaningField = "instructionDocTypeMeaning")
    private String instructionDocType;

    @ApiModelProperty(value = "单据类型含义")
    private String instructionDocTypeMeaning;

    @ApiModelProperty(value = "单据单号")
    private String instructionDocNum;

    @ApiModelProperty(value = "工厂id")
    private String siteId;

    @ApiModelProperty(value = "工厂名称")
    private String siteName;

    @ApiModelProperty(value = "工厂编码")
    private String siteCode;

    @ApiModelProperty(value = "单据状态")
    @LovValue(lovCode = "Z_INSTRUCTION_DOC_STATUS", meaningField = "instructionDocStatusMeaning")
    private String instructionDocStatus;

    @ApiModelProperty(value = "单据状态含义")
    private String instructionDocStatusMeaning;

    @ApiModelProperty(value = "供应商id")
    private String supplierId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "创建时间")
    private Date creationDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "更新人")
    private String lastUpdatedBy;

    @ApiModelProperty(value = "更新人名称")
    private String lastUpdatedByName;

    @ApiModelProperty(value = "退货时间")
    private Date demandTime;
}
