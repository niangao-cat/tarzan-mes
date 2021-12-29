package com.ruike.reports.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.core.base.BaseConstants;
import org.hzero.export.annotation.ExcelColumn;
import org.hzero.export.annotation.ExcelSheet;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * COS工位加工汇总
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/1 15:40
 */
@Data
@ExcelSheet(title = "COS工位加工汇总报表")
public class HmeCosWorkcellSummaryVO {
    @ApiModelProperty("工单ID")
    private String workOrderId;

    @ApiModelProperty("工单")
    @ExcelColumn(title = "工单")
    private String workOrderNum;

    @ApiModelProperty("工单量")
    @ExcelColumn(title = "工单量")
    private BigDecimal woQty;

    @ApiModelProperty("WAFER")
    @ExcelColumn(title = "WAFER")
    private String wafer;

    @ApiModelProperty("COS类型")
    @ExcelColumn(title = "COS类型")
    private String cosType;

    @ApiModelProperty("加工数量")
    @ExcelColumn(title = "加工数量")
    private BigDecimal snQty;

    @ApiModelProperty("合格芯片数")
    @ExcelColumn(title = "合格芯片数")
    private BigDecimal okQty;

    @ApiModelProperty("不良芯片数")
    @ExcelColumn(title = "不良芯片数")
    private BigDecimal ngQty;

    @ApiModelProperty("操作人")
    @ExcelColumn(title = "操作人")
    private String operatorName;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("工位编码")
    @ExcelColumn(title = "工位编码")
    private String workcellCode;

    @ApiModelProperty("工位描述")
    @ExcelColumn(title = "工位描述")
    private String workcellName;

    @ApiModelProperty("时间")
    @ExcelColumn(title = "时间", pattern = BaseConstants.Pattern.DATE)
    private Date creationDate;

    @ApiModelProperty("工位设备")
    @ExcelColumn(title = "工位设备")
    private String equipment;

    @ApiModelProperty("产品编码")
    @ExcelColumn(title = "产品编码")
    private String materialCode;

    @ApiModelProperty("产品描述")
    @ExcelColumn(title = "产品描述")
    private String materialName;

    @ApiModelProperty("工艺ID")
    private String operationId;

    @ApiModelProperty("工艺编码")
    @ExcelColumn(title = "工艺编码")
    private String operationCode;

    @ApiModelProperty("工艺描述")
    @ExcelColumn(title = "工艺描述")
    private String operationName;

    @ApiModelProperty("wafer芯片数")
    @ExcelColumn(title = "wafer芯片数")
    private Long waferNum;

    @ApiModelProperty("创建人")
    private Long operatorId;

    @ApiModelProperty("工序作业ID")
    private String jobId;

    @ApiModelProperty("cos数量")
    private Long cosNum;

    public static HmeCosWorkcellSummaryVO summary(HmeCosWorkcellSummaryVO obj) {
        HmeCosWorkcellSummaryVO result = new HmeCosWorkcellSummaryVO();
        result.setWorkOrderId(obj.getWorkOrderId());
        result.setWorkOrderNum(obj.getWorkOrderNum());
        result.setWoQty(obj.getWoQty());
        result.setWafer(obj.getWafer());
        result.setCosType(obj.getCosType());
        result.setOperatorId(obj.getOperatorId());
        result.setWorkcellId(obj.getWorkcellId());
        result.setWorkcellCode(obj.getWorkcellCode());
        result.setWorkcellName(obj.getWorkcellName());
        result.setCreationDate(obj.getCreationDate());
        result.setEquipment(obj.getEquipment());
        result.setMaterialCode(obj.getMaterialCode());
        result.setMaterialName(obj.getMaterialName());
        result.setOperationId(obj.getOperationId());
        result.setOperationCode(obj.getOperationCode());
        result.setOperationName(obj.getOperationName());
        result.setEquipment(obj.getEquipment());
        result.setCosNum(obj.getCosNum());
        result.setSnQty(obj.getSnQty());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HmeCosWorkcellSummaryVO that = (HmeCosWorkcellSummaryVO) o;
        return Objects.equals(workOrderId, that.workOrderId) && Objects.equals(workOrderNum, that.workOrderNum) && Objects.equals(woQty, that.woQty) && Objects.equals(wafer, that.wafer) && Objects.equals(cosType, that.cosType) && Objects.equals(snQty, that.snQty) && Objects.equals(okQty, that.okQty) && Objects.equals(ngQty, that.ngQty) && Objects.equals(operatorName, that.operatorName) && Objects.equals(workcellId, that.workcellId) && Objects.equals(workcellCode, that.workcellCode) && Objects.equals(workcellName, that.workcellName) && Objects.equals(creationDate, that.creationDate) && Objects.equals(equipment, that.equipment) && Objects.equals(materialCode, that.materialCode) && Objects.equals(materialName, that.materialName) && Objects.equals(operationId, that.operationId) && Objects.equals(operationCode, that.operationCode) && Objects.equals(operationName, that.operationName) && Objects.equals(waferNum, that.waferNum) && Objects.equals(operatorId, that.operatorId) && Objects.equals(jobId, that.jobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workOrderId, workOrderNum, woQty, wafer, cosType, snQty, okQty, ngQty, operatorName, workcellId, workcellCode, workcellName, creationDate, equipment, materialCode, materialName, operationId, operationCode, operationName, waferNum, operatorId, jobId);
    }
}
