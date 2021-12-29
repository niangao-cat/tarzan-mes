package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.util.Date;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-05-18 12:59
 */
@Data
public class WmsMaterialExchangeDocDTO {

    @ApiModelProperty(value = "单据ID")
    private String instructionDocId;
    @ApiModelProperty(value = "单据编码")
    private String instructionDocNum;
    @ApiModelProperty(value = "单据状态")
    @LovValue(lovCode = "WMS.SUPPLIER_EXCHANGE_DOC.STATUS",meaningField="instructionDocStatusMeaning",defaultMeaning = "无")
    private String instructionDocStatus;
    @ApiModelProperty(value = "单据状态说明")
    private String instructionDocStatusMeaning;
    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "工厂编码")
    private String siteCode;
    @ApiModelProperty(value = "工厂名称")
    private String siteName;
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "申请人ID")
    private Long createdBy;
    @ApiModelProperty(value = "申请人名称")
    private String createdByName;
    @ApiModelProperty(value = "创建时间")
    private Date creationDate;
    @ApiModelProperty(value = "备注")
    private String remark;
}
