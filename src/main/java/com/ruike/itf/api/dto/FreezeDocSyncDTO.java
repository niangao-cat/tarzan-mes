package com.ruike.itf.api.dto;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.domain.repository.HmeFreezeDocRepository;
import com.ruike.hme.domain.vo.HmeFreezeDocVO;
import com.ruike.hme.infra.util.BeanCopierUtil;
import com.ruike.itf.domain.entity.ItfFreezeDocIface;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 冻结单同步
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/3 15:39
 */
@Data
public class FreezeDocSyncDTO implements Serializable {
    private static final long serialVersionUID = 2103357070690146116L;

    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("接口表主键")
    private String interfaceId;
    @ApiModelProperty(value = "冻结单号")
    private String freezeDocNum;
    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "冻结类型")
    private String freezeType;
    @ApiModelProperty(value = "冻结状态")
    private String freezeDocStatus;
    @ApiModelProperty(value = "审批状态")
    private String approvalStatus;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "生产版本")
    private String productionVersion;
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "库存批次")
    private String inventoryLot;
    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;
    @ApiModelProperty(value = "工单ID")
    private String workOrderId;
    @ApiModelProperty(value = "设备")
    private String equipmentId;
    @ApiModelProperty(value = "生产线")
    private String prodLineId;
    @ApiModelProperty(value = "工段")
    private String workcellIdLine;
    @ApiModelProperty(value = "工序")
    private String workcellIdProcess;
    @ApiModelProperty(value = "工位")
    private String workcellIdStation;
    @ApiModelProperty(value = "操作人ID")
    private String userId;
    @ApiModelProperty(value = "COS类型")
    private String cosType;
    @ApiModelProperty(value = "WAFER")
    private String wafer;
    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;
    @ApiModelProperty(value = "金锡比")
    private String ausnRatio;
    @ApiModelProperty(value = "热沉编号")
    private String hotSinkNum;
    @ApiModelProperty(value = "筛选规则")
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
    @ApiModelProperty(value = "创建人")
    private String createBy;
    @ApiModelProperty(value = "序号")
    private String sequence;

    public static FreezeDocSyncDTO toSync(ItfFreezeDocIface iface, HmeFreezeDocRepository freezeDocRepository) {
        FreezeDocSyncDTO dto = new FreezeDocSyncDTO();
        HmeFreezeDocVO representation = freezeDocRepository.byId(iface.getTenantId(), iface.getFreezeDocId());
        BeanCopierUtil.copy(iface, dto);

        dto.id = iface.getFreezeDocId();
        dto.interfaceId = iface.getInterfaceId();
        dto.productionDateFrom = iface.getProductionDateFrom();
        dto.productionDateTo = iface.getProductionDateTo();
        dto.sequence = String.valueOf(iface.getSequence());
        dto.ausnRatio = Objects.isNull(iface.getAusnRatio()) ? "" : iface.getAusnRatio().toPlainString();
        dto.freezeType = representation.getFreezeTypeMeaning();
        dto.freezeDocStatus = representation.getFreezeDocStatusMeaning();
        dto.approvalStatus = representation.getApprovalStatusMeaning();
        dto.siteId = representation.getSiteCode();
        dto.materialId = representation.getMaterialCode();
        dto.warehouseId = representation.getWarehouseCode();
        dto.locatorId = representation.getLocatorCode();
        dto.materialId = representation.getMaterialCode();
        dto.supplierId = representation.getSupplierCode();
        dto.workOrderId = representation.getWorkOrderNum();
        dto.equipmentId = representation.getEquipmentCode();
        dto.prodLineId = representation.getProdLineCode();
        dto.workcellIdLine = representation.getWorkcellCode();
        dto.workcellIdProcess = representation.getProcessCode();
        dto.workcellIdStation = representation.getStationCode();
        dto.userId = representation.getOperatedByName();
        dto.createBy = representation.getCreatedByName();
        return dto;
    }


}
