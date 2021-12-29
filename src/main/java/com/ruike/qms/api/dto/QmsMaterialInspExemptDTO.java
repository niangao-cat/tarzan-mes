package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-04-26 12:59
 */
@Data
public class QmsMaterialInspExemptDTO {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "免检ID")
    private String exemptionId;
    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "工厂编码")
    private String siteCode;
    @ApiModelProperty(value = "工厂名称")
    private String siteName;
    @ApiModelProperty(value = "免检类型")
    private String exemptionType;
    @ApiModelProperty(value = "免检类型名称")
    private String exemptionTypeName;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "供应商地点ID")
    private String supplierSiteId;
    @ApiModelProperty(value = "供应商地点编码")
    private String supplierSiteCode;
    @ApiModelProperty(value = "供应商地点名称")
    private String supplierSiteName;
    @ApiModelProperty(value = "免检标志")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "exemptionFlagMeaning")
    private String exemptionFlag;
    @ApiModelProperty(value = "免检标志描述")
    private String exemptionFlagMeaning;
    @ApiModelProperty(value = "有效标志")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;
    @ApiModelProperty(value = "有效标志描述")
    private String enableFlagMeaning;

}
