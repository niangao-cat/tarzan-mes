package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-05-18 12:59
 */
@Data
public class WmsMaterialExchangeParamDTO {

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty(value = "单据编码")
    private String instructionDocNum;
    @ApiModelProperty(value = "单据状态")
    private String instructionDocStatus;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料描述")
    private String materialName;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
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
    @ApiModelProperty(value = "创建时间")
    private Date createDateFrom;
    @ApiModelProperty(value = "创建时间")
    private Date createDateTo;
}
