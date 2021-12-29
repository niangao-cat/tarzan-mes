package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 条码解冻单 创建条码
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/23 16:01
 */
@Data
public class HmeFreezeDocCreateSnVO {
    @ApiModelProperty(value = "序号")
    private Integer sequenceNum;
    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "物料批")
    private String materialLotCode;
    @ApiModelProperty(value = "冻结标志")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "snFreezeFlagMeaning")
    private String snFreezeFlag;
    @ApiModelProperty(value = "冻结标志含义")
    private String snFreezeFlagMeaning;
    @ApiModelProperty(value = "冻结时间")
    private Date freezeDate;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "条码数量")
    private BigDecimal primaryUomQty;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "生产版本")
    private String productionVersion;
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;
    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "库存批次")
    private String inventoryLot;
    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;
    @ApiModelProperty("工序作业ID")
    private String jobId;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "工单")
    private String workOrderNum;
    @ApiModelProperty(value = "实验代码")
    private String testCode;
    @ApiModelProperty(value = "设备")
    private String equipmentId;
    @ApiModelProperty(value = "设备编码")
    private String equipmentCode;
    @ApiModelProperty(value = "生产线")
    private String prodLineId;
    @ApiModelProperty(value = "生产线编码")
    private String prodLineCode;
    @ApiModelProperty(value = "工段")
    private String workcellId;
    @ApiModelProperty(value = "工段编码")
    private String workcellCode;
    @ApiModelProperty(value = "工序")
    private String processId;
    @ApiModelProperty(value = "工序编码")
    private String processCode;
    @ApiModelProperty(value = "工位")
    private String stationId;
    @ApiModelProperty(value = "工位编码")
    private String stationCode;
    @ApiModelProperty(value = "操作人ID")
    private Long operatedBy;
    @ApiModelProperty(value = "操作人姓名")
    private String operatedByName;
    @ApiModelProperty(value = "生产时间")
    private Date productionDate;
    @ApiModelProperty(value = "在制标识")
    @LovValue(lovCode = "WMS.FLAG_YN", meaningField = "mfFlagMeaning")
    private String mfFlag;
    @ApiModelProperty(value = "在制标识含义")
    private String mfFlagMeaning;
    @ApiModelProperty(value = "COS类型")
    private String cosType;

    public HmeFreezeDocCreateSnVO propertiesCompletion(HmeFreezeDocJobVO vo) {
        this.setWorkOrderId(vo.getWorkOrderId());
        this.setWorkOrderNum(vo.getWorkOrderNum());
        this.setEquipmentId(vo.getEquipmentId());
        this.setEquipmentCode(vo.getEquipmentCode());
        this.setProdLineId(vo.getProdLineCode());
        this.setProdLineCode(vo.getProdLineCode());
        this.setStationId(vo.getStationId());
        this.setStationCode(vo.getStationCode());
        this.setProcessId(vo.getProcessId());
        this.setProcessCode(vo.getProcessCode());
        this.setWorkcellId(vo.getWorkcellId());
        this.setWorkcellCode(vo.getWorkcellCode());
        this.setOperatedBy(vo.getOperatedBy());
        this.setOperatedByName(vo.getOperatedByName());
        this.setProductionDate(vo.getProductionDate());
        return this;
    }
}
