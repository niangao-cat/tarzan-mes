package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.math.BigDecimal;
import java.util.List;

/**
 * 备料执行条码信息
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 14:56
 */
@Data
public class WmsPrepareExecuteBarcodeVO {
    @ApiModelProperty(value = "条码ID")
    private String loadObjectId;
    @ApiModelProperty(value = "条码类型")
    private String loadObjectType;
    @ApiModelProperty(value = "单位ID")
    private String uomId;
    @ApiModelProperty(value = "主单位数量")
    private BigDecimal primaryUomQty;
    @ApiModelProperty(value = "超出数量标志")
    private Boolean overloadFlag;
    @ApiModelProperty(value = "配送单行状态")
    @LovValue(lovCode = "WMS.DISTRIBUTION_LINE_STATUS", meaningField = "instructionStatusMeaning")
    private String instructionStatus;
    @ApiModelProperty(value = "配送单行状态")
    private String instructionStatusMeaning;
    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "物料批ID列表")
    private List<WmsMaterialLotAttrVO> materialLotList;
}
