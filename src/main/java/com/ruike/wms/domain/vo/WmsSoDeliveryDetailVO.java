package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.boot.platform.lov.annotation.LovValue;
import tarzan.actual.domain.entity.MtInstructionActualDetail;

import java.util.Date;

/**
 * <p>
 * 发货单明细
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/10 10:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WmsSoDeliveryDetailVO extends MtInstructionActualDetail {
    private static final long serialVersionUID = -6114052338094534523L;

    @ApiModelProperty("单据头ID")
    private String instructionDocId;
    @ApiModelProperty("单据行ID")
    private String instructionId;
    @ApiModelProperty("物料批编码")
    private String materialLotCode;
    @ApiModelProperty("物料批状态")
    @LovValue(lovCode = "WMS.MTLOT.STATUS", meaningField = "materialLotStatusMeaning")
    private String materialLotStatus;
    @ApiModelProperty("物料批状态含义")
    private String materialLotStatusMeaning;
    @ApiModelProperty("质量状态")
    @LovValue(lovCode = "WMS.MTLOT.QUALITY_STATUS", meaningField = "qualityStatusMeaning")
    private String qualityStatus;
    @ApiModelProperty("质量状态含义")
    private String qualityStatusMeaning;
    @ApiModelProperty("容器条码")
    private String containerCode;
    @ApiModelProperty("物料")
    private String materialId;
    @ApiModelProperty("物料编码")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("单位")
    private String uomCode;
    @ApiModelProperty("销售订单号")
    private String soNum;
    @ApiModelProperty("销售订单行号")
    private String soLineNum;
    @ApiModelProperty("批次")
    private String lot;
    @ApiModelProperty("生产订单ID")
    private String workOrderId;
    @ApiModelProperty("生产订单号")
    private String workOrderNum;
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("仓库编码")
    private String warehouseCode;
    @ApiModelProperty("货位编码")
    private String locatorCode;
    @ApiModelProperty("发货时间")
    private Date deliveryDate;
    @ApiModelProperty("执行人")
    private String lastUpdatedByName;
    @ApiModelProperty("有效性")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;
    @ApiModelProperty("有效性含义")
    private String enableFlagMeaning;
    @ApiModelProperty("mt_instruction实发数")
    private Double lineActualQty;
}
