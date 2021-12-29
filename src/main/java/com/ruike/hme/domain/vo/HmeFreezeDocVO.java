package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeFreezeDoc;
import com.ruike.hme.infra.util.BeanCopierUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 条码冻结单 查询结果
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/22 15:52
 */
@Data
public class HmeFreezeDocVO {
    @ApiModelProperty("主键")
    private String freezeDocId;
    @ApiModelProperty(value = "序号")
    private Integer sequenceNum;
    @ApiModelProperty(value = "冻结单号")
    private String freezeDocNum;
    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "工厂编码")
    private String siteCode;
    @ApiModelProperty(value = "冻结类型")
    @LovValue(lovCode = "HME_FREEZE_TYPE", meaningField = "freezeTypeMeaning")
    private String freezeType;
    @ApiModelProperty(value = "冻结类型含义")
    private String freezeTypeMeaning;
    @ApiModelProperty(value = "冻结状态")
    @LovValue(lovCode = "HME_FREEZE_STATUS", meaningField = "freezeDocStatusMeaning")
    private String freezeDocStatus;
    @ApiModelProperty(value = "冻结状态含义")
    private String freezeDocStatusMeaning;
    @ApiModelProperty(value = "审批状态")
    @LovValue(lovCode = "HME_FREEZE_APPROVAL_STATUS", meaningField = "approvalStatusMeaning")
    private String approvalStatus;
    @ApiModelProperty(value = "审批状态含义")
    private String approvalStatusMeaning;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
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
    @ApiModelProperty(value = "COS类型")
    @LovValue(lovCode = "HME_COS_TYPE", meaningField = "cosTypeMeaning")
    private String cosType;
    @ApiModelProperty(value = "COS类型含义")
    private String cosTypeMeaning;
    @ApiModelProperty(value = "WAFER")
    private String wafer;
    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;
    @ApiModelProperty(value = "金锡比")
    private BigDecimal ausnRatio;
    @ApiModelProperty(value = "热沉编号")
    private String hotSinkNum;
    @ApiModelProperty(value = "筛选规则")
    private String cosRuleId;
    @ApiModelProperty(value = "筛选规则编码")
    private String cosRuleCode;
    @ApiModelProperty(value = "班次")
    private String shiftId;
    @ApiModelProperty(value = "班次编码")
    private String shiftCode;
    @ApiModelProperty(value = "生产时间从")
    private Date productionDateFrom;
    @ApiModelProperty(value = "生产时间至")
    private Date productionDateTo;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty(value = "创建人")
    private String createdByName;

    public HmeFreezeDoc toEntity() {
        HmeFreezeDoc eo = new HmeFreezeDoc();
        BeanCopierUtil.copy(this, eo);
        return eo;
    }

}
