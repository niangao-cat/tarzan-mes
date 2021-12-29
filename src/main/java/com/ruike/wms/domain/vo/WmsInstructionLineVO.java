package com.ruike.wms.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Transient;

import org.hzero.boot.platform.lov.annotation.LovValue;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * WmsInstructionLineVO
 *
 * @author liyuan.lv@hand-china.com 2020/04/04 22:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WmsInstructionLineVO implements Serializable {

    private static final long serialVersionUID = 4942253858816696526L;
    @ApiModelProperty("单据ID")
    private String instructionDocId;
    @ApiModelProperty("单据Num")
    private String instructionDocNum;
    @ApiModelProperty("单据类型")
    private String instructionDocType;
    @ApiModelProperty("单据状态")
    @LovValue(value = "WMS.DELIVERY_DOC.STATUS",meaningField ="instructionDocStatusMeaning" )
    private String instructionDocStatus;
    @ApiModelProperty("指令ID")
    private String instructionId;
    @ApiModelProperty("指令Num")
    private String instructionNum;
    @ApiModelProperty("指令类型")
    private String instructionType;
    @ApiModelProperty("指令状态")
//    @LovValue(value = "WMS.DELIVERY_DOC_LINE.STATUS",meaningField ="instructionStatusMeaning" )
    private String instructionStatus;
    @ApiModelProperty("指令状态值集")
    private String instructionStatusLov;
    @ApiModelProperty("工厂id")
    private String siteId;
    @ApiModelProperty("工厂code")
    private String siteCode;
    @ApiModelProperty("工厂名称")
    private String siteName;
    @ApiModelProperty("供应商id")
    private String supplierId;
    @ApiModelProperty("供应商code")
    private String supplierCode;
    @ApiModelProperty("供应商名称")
    private String supplierName;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料code")
    private String materialCode;
    @ApiModelProperty("物料名称")
    private String materialName;
    @ApiModelProperty("物料版本")
    private String materialVersion;
    @ApiModelProperty("已接收数量")
    private BigDecimal receivedQty = BigDecimal.valueOf(0);
    @ApiModelProperty("实际接收数量")
    private BigDecimal actualReceiveQty = BigDecimal.valueOf(0);
    @ApiModelProperty("料废调换数量")
    private BigDecimal exchangeQty = BigDecimal.valueOf(0);
    @ApiModelProperty("已料废调换数量")
    private BigDecimal exchangedQty = BigDecimal.valueOf(0);
    @ApiModelProperty("已执行料废调换数量")
    private BigDecimal stockInExchangedQty = BigDecimal.valueOf(0);
    @ApiModelProperty("数量")
    private BigDecimal quantity = BigDecimal.valueOf(0);
    @ApiModelProperty("检验报废数量")
    private BigDecimal inspectScrapQty = BigDecimal.valueOf(0);
    @ApiModelProperty("已入库上架数量")
    private BigDecimal stockInQty = BigDecimal.valueOf(0);
    @ApiModelProperty("单位ID")
    private String uomId;
    @ApiModelProperty("单位code")
    private String uomCode;
    @ApiModelProperty("单位描述")
    private String uomName;
    @ApiModelProperty("批次")
    private String lot;
    @ApiModelProperty("条码id")
    private String materialLotId;
    @ApiModelProperty("条码code")
    private String materialLotCode;
    @ApiModelProperty("货位ID")
    private String locatorId;
    @ApiModelProperty("货位code")
    private String locatorCode;
    @ApiModelProperty("货位名称")
    private String locatorName;
    @ApiModelProperty("条码货位ID")
    private String materialLotLocatorId;
    @ApiModelProperty("条码货位code")
    private String materialLotLocatorCode;
    @ApiModelProperty("条码货位名称")
    private String materialLotLocatorName;
    @ApiModelProperty("质量状态")
    @LovValue(value = "WMS.MTLOT.QUALITY_STATUS",meaningField ="qualityStatusMeaning" )
    private String qualityStatus;
    @ApiModelProperty("质量状态Meaning")
    private String qualityStatusMeaning;
    @ApiModelProperty("条码状态")
    @LovValue(value = "WMS.MTLOT.STATUS",meaningField ="materialLotStatusMeaning" )
    private String materialLotStatus;
    @ApiModelProperty("条码状态Meaning")
    private String materialLotStatusMeaning;
    @ApiModelProperty("容器ID")
    private String containerId;
    @ApiModelProperty("容器Code")
    private String containerCode;
    @ApiModelProperty("扫描条码")
    private String barCode;

    @ApiModelProperty("单据状态Meaning")
    private String instructionDocStatusMeaning;
    @ApiModelProperty("单据行状态Meaning")
    private String instructionStatusMeaning;
    @ApiModelProperty("仓库id")
    private String warehouseId;
    @ApiModelProperty("仓库编码")
    private String warehouseCode;
    @ApiModelProperty("是否启用")
    private String enableFlag;
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("条码总数汇总")
    private BigDecimal sumCount;
    @ApiModelProperty("已入库条码数量汇总")
    private BigDecimal sumStockInCount;
    @ApiModelProperty("已执行数量汇总")
    private BigDecimal sumStockInQty;

    @ApiModelProperty("接收完成时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date actualReceivedDate;
    @ApiModelProperty("是否免检")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "exemptionFlagMeaning")
    private String exemptionFlag;
    @ApiModelProperty("检验单ID")
    private String iqcHeaderId;
    @ApiModelProperty("检验单")
    private String iqcNumber;
    @ApiModelProperty("检验单类型")
    @LovValue(value = "QMS.DOC_INSPECTION_TYPE", meaningField = "inspectionTypeMeaning")
    private String inspectionType;
    @ApiModelProperty("检验状态")
    @LovValue(value = "QMS.INSPECTION_DOC_STATUS", meaningField = "inspectionStatusMeaning")
    private String inspectionStatus;
    @ApiModelProperty("检验结果")
    @LovValue(value = "QMS.INSPECTION_RESULT", meaningField = "inspectionResultMeaning")
    private String inspectionResult;
    @ApiModelProperty("审核结果")
    @LovValue(value = "QMS.FINAL_DECISION", meaningField = "finalDecisionMeaning")
    private String finalDecision;

    @ApiModelProperty("检验完成时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date inspectionFinishDate;

    @ApiModelProperty("行号")
    private String instructionLineNum;

    @ApiModelProperty("是否免检Meaning")
    private String exemptionFlagMeaning;

    @ApiModelProperty("送货单类型Meaning")
    private String inspectionTypeMeaning;

    @ApiModelProperty("检验状态Meaning")
    private String inspectionStatusMeaning;

    @ApiModelProperty("检验结果Meaning")
    private String inspectionResultMeaning;

    @ApiModelProperty("审核结果Meaning")
    private String finalDecisionMeaning;

    @ApiModelProperty("库存调拨指令Id")
    private String transOverInstructionId;

    @ApiModelProperty("库存调拨检验状态")
    private String transOverInspectionStatus;

    @ApiModelProperty("库存调拨指令状态")
    private String transOverInstructionStatus;

    @ApiModelProperty("采购订单")
    private String poNumber;
    @ApiModelProperty("采购订单行")
    private String poLineNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WmsInstructionLineVO that = (WmsInstructionLineVO) o;
        return Objects.equals(containerId, that.containerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerId);
    }
}
