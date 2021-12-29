package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-04-26 12:59
 */
@Data
public class QmsMaterialInspExemptDTO4 {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty("主键ID，表示唯一一条记录")
    private String exemptionId;
    @ApiModelProperty(value = "组织ID",required = true)
    private String siteId;
    @ApiModelProperty(value = "供应商ID",required = true)
    private String supplierId;
    @ApiModelProperty(value = "供应商地址ID",required = true)
    private String supplierSiteId;
    @ApiModelProperty(value = "物料ID",required = true)
    private String materialId;
    @ApiModelProperty(value = "免检标识",required = true)
    private String exemptionFlag;
    @ApiModelProperty(value = "是否有效",required = true)
    private String enableFlag;
    @ApiModelProperty(value = "免检类型",required = true)
    private String exemptionType;
}
