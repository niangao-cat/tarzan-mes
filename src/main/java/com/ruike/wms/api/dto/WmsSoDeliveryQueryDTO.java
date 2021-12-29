package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 14:04
 */
@Data
public class WmsSoDeliveryQueryDTO {
    @ApiModelProperty("出货单号, 支持模糊查询")
    private String instructionDocNum;
    @ApiModelProperty("出货单ID")
    private String instructionDocId;
    @ApiModelProperty("单据状态")
    private String instructionDocStatus;
    @ApiModelProperty("工厂ID")
    private String siteId;
    @ApiModelProperty("客户编码")
    private String customerCode;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("单据类型")
    private String instructionDocType;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("目标仓库")
    private String locatorId;
    @ApiModelProperty("更新人")
    private Long lastUpdatedBy;
    @ApiModelProperty(value = "创建时间从")
    private String creationDateFrom;
    @ApiModelProperty(value = "创建时间至")
    private String creationDateTo;
}
