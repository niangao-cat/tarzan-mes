package com.ruike.hme.api.dto;

import io.choerodon.core.exception.CommonException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 条码冻结单 查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/22 16:20
 */
@Data
public class HmeFreezeDocQueryDTO implements Serializable {

    private static final long serialVersionUID = -7807592594729690016L;

    @ApiModelProperty(value = "冻结单Id")
    private String freezeDocId;
    @ApiModelProperty(value = "冻结单号")
    private String freezeDocNum;
    @ApiModelProperty(value = "工厂ID")
    private String siteId;
    @ApiModelProperty(value = "冻结类型标记,传的是值集的值")
    private String freezeType;
    @ApiModelProperty(value = "冻结状态")
    private String freezeDocStatus;
    @ApiModelProperty(value = "审批状态")
    private String approvalStatus;
    @ApiModelProperty(value = "物料Id")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "生产版本")
    private String productionVersion;
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "供应商ID")
    private String supplierId;
    @ApiModelProperty(value = "库存批次")
    private String inventoryLot;
    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;
    @ApiModelProperty(value = "工单Id")
    private String workOrderId;
    @ApiModelProperty(value = "工单号")
    private String workOrderNum;
    @ApiModelProperty(value = "实验代码")
    private String testCode;
    @ApiModelProperty(value = "设备")
    private String equipmentId;
    @ApiModelProperty(value = "生产线")
    private String prodLineId;
    @ApiModelProperty(value = "工段")
    private String workcellId;
    @ApiModelProperty(value = "工序")
    private String processId;
    @ApiModelProperty(value = "工位")
    private String stationId;
    @ApiModelProperty(value = "操作人ID")
    private Long operatedBy;
    @ApiModelProperty(value = "COS类型")
    private String cosType;
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
    @ApiModelProperty(value = "班次")
    private String shiftId;
    @ApiModelProperty(value = "生产时间从")
    private Date productionDateFrom;
    @ApiModelProperty(value = "生产时间至")
    private Date productionDateTo;
    @ApiModelProperty(value = "冻结状态集合")
    private List<String> freezeDocStatusList;
    @ApiModelProperty(value = "采购件序列号,以逗号分隔的字符串")
    private String purchasedSn;
    @ApiModelProperty(value = "冻结类型标记,传的是值集的标记，后端自用")
    private String freezeTypeTag;
    @ApiModelProperty(value = "多个物料标识,后端自用")
    private String materialListFlag;


    @ApiModelProperty(value = "WAFER", hidden = true)
    private Set<String> wafers;
    @ApiModelProperty(value = "虚拟号", hidden = true)
    private Set<String> virtualNums;
    @ApiModelProperty(value = "热沉编号", hidden = true)
    private Set<String> hotSinkNums;
    @ApiModelProperty(value = "物料批ID集合", hidden = true)
    private List<String> materialLotIdList;
    @ApiModelProperty(value = "筛选时间从", hidden = true)
    private Date preSelectionDateFrom;
    @ApiModelProperty(value = "筛选时间至", hidden = true)
    private Date preSelectionDateTo;

    public static boolean jobValidNeeded(HmeFreezeDocQueryDTO dto) {
        return (StringUtils.isNotBlank(dto.getWorkOrderNum()) || StringUtils.isNotBlank(dto.getWorkOrderId()) || StringUtils.isNotBlank(dto.getEquipmentId()) || StringUtils.isNotBlank(dto.getProdLineId()) || StringUtils.isNotBlank(dto.getWorkcellId()) || StringUtils.isNotBlank(dto.getStationId()) || Objects.nonNull(dto.getOperatedBy()) || StringUtils.isNotBlank(dto.getShiftId()) || Objects.nonNull(dto.getProductionDateFrom()) || Objects.nonNull(dto.getProductionDateTo()))
                && ("P_INVENTORY".equals(dto.getFreezeType()) || "COS_P_INVENTORY".equals(dto.getFreezeType()));
    }

    public void paramInit() {
        if (StringUtils.isBlank(this.getSiteId()) || StringUtils.isBlank(this.getFreezeType()) || StringUtils.isBlank(this.getMaterialId())) {
            throw new CommonException("存在必输的查询参数未输入");
        }
        if("P_INVENTORY".equals(this.getFreezeType()) || "M_INVENTORY".equals(this.getFreezeType())){
            this.setFreezeTypeTag("INVENTORY");
        } else if("COS_P_INVENTORY".equals(this.getFreezeType()) || "COS_CHIP_INVENTORY".equals(this.getFreezeType())
                || "COS_M_INVENTORY".equals(this.getFreezeType())){
            this.setFreezeTypeTag("COS_INVENTORY");
        }
        if(this.getMaterialId().contains(",")){
            this.setMaterialListFlag("Y");
        }else {
            this.setMaterialListFlag("N");
        }
        //为了考虑到COS贴片后冻结时，输入了筛选规则，生产时间只为筛选时间的情况
        if("COS_M_INVENTORY".equals(this.getFreezeType()) && StringUtils.isNotBlank(this.getCosRuleId()) && Objects.nonNull(this.getProductionDateFrom())){
            this.setPreSelectionDateFrom(this.getProductionDateFrom());
            this.setProductionDateFrom(null);
        }
        if("COS_M_INVENTORY".equals(this.getFreezeType()) && StringUtils.isNotBlank(this.getCosRuleId()) && Objects.nonNull(this.getProductionDateTo())){
            this.setPreSelectionDateTo(this.getProductionDateTo());
            this.setProductionDateTo(null);
        }
        this.setWafers(StringUtils.isBlank(this.getWafer()) ? new HashSet<>() : new HashSet<>(Arrays.asList(StringUtils.split(this.getWafer(), ","))));
        this.setVirtualNums(StringUtils.isBlank(this.getVirtualNum()) ? new HashSet<>() : new HashSet<>(Arrays.asList(StringUtils.split(this.getVirtualNum(), ","))));
        this.setHotSinkNums(StringUtils.isBlank(this.getHotSinkNum()) ? new HashSet<>() : new HashSet<>(Arrays.asList(StringUtils.split(this.getHotSinkNum(), ","))));
    }
}
