package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 发货单明细 查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/10 10:26
 */
@Data
public class WmsSoDeliveryDetailQueryDTO {
    @ApiModelProperty(value = "出货单ID", required = true)
    private String instructionDocId;
    @ApiModelProperty(value = "行ID列表，用逗号隔开", required = true)
    private String instructionIdList;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty(value = "物料批状态")
    private String materialLotStatus;
    @ApiModelProperty("质量状态")
    private String qualityStatus;
    @ApiModelProperty("容器编码")
    private String containerCode;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("发货时间从")
    private Date deliveryDateFrom;
    @ApiModelProperty("发货时间至")
    private Date deliveryDateTo;
    @ApiModelProperty("货位ID")
    private String locatorId;
}
