package com.ruike.mdm.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 货位导入
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/3 14:35
 */
@Data
public class MdmLocatorImportDTO {
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
    @ApiModelProperty(value = "工厂id")
    private String organizationId;
    @ApiModelProperty(value = "货位1")
    private String warehouseCode;
    @ApiModelProperty(value = "货位2")
    private String locatorCode;
    @ApiModelProperty(value = "货位名称中文")
    private String locatorNameZh;
    @ApiModelProperty(value = "货位描述英文")
    private String locatorNameUs;
    @ApiModelProperty(value = "货位类型")
    private String locatorType;
    @ApiModelProperty(value = "货位位置")
    private String locatorLocation;
}
