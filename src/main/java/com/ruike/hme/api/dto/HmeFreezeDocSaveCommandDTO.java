package com.ruike.hme.api.dto;

import com.ruike.hme.domain.entity.HmeFreezeDoc;
import com.ruike.hme.domain.entity.HmeFreezeDocLine;
import com.ruike.hme.infra.util.BeanCopierUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * <p>
 * 条码冻结单 保存命令
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 10:08
 */
@Data
public class HmeFreezeDocSaveCommandDTO implements Serializable {
    private static final long serialVersionUID = -9178044934956731148L;

    @ApiModelProperty(value = "冻结单Id")
    private String freezeDocId;
    @ApiModelProperty(value = "冻结单号")
    private String freezeDocNum;
    @ApiModelProperty(value = "工厂ID")
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "工厂编码")
    private String siteCode;
    @ApiModelProperty(value = "冻结类型")
    @NotBlank
    private String freezeType;
    @ApiModelProperty(value = "冻结状态")
    private String freezeDocStatus;
    @ApiModelProperty(value = "审批状态")
    private String approvalStatus;
    @ApiModelProperty(value = "物料ID")
    @NotBlank
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
    @ApiModelProperty(value = "工位")
    private String stationId;
    @ApiModelProperty(value = "工位编码")
    private String stationCode;
    @ApiModelProperty(value = "操作人ID")
    private Long operatedBy;
    @ApiModelProperty(value = "操作人姓名")
    private String operatedByName;
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
    @ApiModelProperty(value = "WAFER")
    private String wafer;
    @ApiModelProperty(value = "工序")
    private String processId;
    @ApiModelProperty(value = "虚拟号")
    private String virtualNum;
    @ApiModelProperty(value = "金锡比")
    private BigDecimal ausnRatio;
    @ApiModelProperty(value = "热沉编号")
    private String hotSinkNum;
    @ApiModelProperty(value = "筛选规则")
    private String cosRuleId;
    @ApiModelProperty(value = "COS类型")
    private String cosType;

    @ApiModelProperty("冻结行")
    @NotEmpty
    private List<SaveLineCommand> lineList;

    public static HmeFreezeDoc toEntity(HmeFreezeDocSaveCommandDTO dto) {
        HmeFreezeDoc eo = new HmeFreezeDoc();
        BeanCopierUtil.copy(dto, eo);
        return eo;
    }

    public static HmeFreezeDocSaveCommandDTO toCommand(HmeFreezeDoc dto) {
        HmeFreezeDocSaveCommandDTO co = new HmeFreezeDocSaveCommandDTO();
        BeanCopierUtil.copy(dto, co);
        return co;
    }

    public static HmeFreezeDocQueryDTO toQuery(HmeFreezeDocSaveCommandDTO dto) {
        HmeFreezeDocQueryDTO query = new HmeFreezeDocQueryDTO();
        BeanCopierUtil.copy(dto, query);
        query.paramInit();
        return query;
    }

    public static List<HmeFreezeDocLine> lineBatchToEntity(List<SaveLineCommand> dtoList) {
        List<HmeFreezeDocLine> lineList = new ArrayList<>();
        dtoList.forEach(r -> lineList.add(SaveLineCommand.toEntity(r)));
        return lineList;
    }

    @Data
    public static class SaveLineCommand {
        @ApiModelProperty("冻结单行ID")
        private String freezeDocLineId;
        @ApiModelProperty("冻结单ID")
        private String freezeDocId;
        @ApiModelProperty("物料批ID")
        @NotBlank
        private String materialLotId;
        @ApiModelProperty("条码冻结标识")
        private String snFreezeFlag;
        @ApiModelProperty("条码在制标识")
        private String mfFlag;
        @ApiModelProperty("冻结标识")
        private String freezeFlag;
        @ApiModelProperty("冻结人ID")
        private Long frozenBy;
        @ApiModelProperty("租户ID")
        private Long tenantId;

        public static HmeFreezeDocLine toEntity(SaveLineCommand dto) {
            HmeFreezeDocLine eo = new HmeFreezeDocLine();
            BeanCopierUtil.copy(dto, eo);
            return eo;
        }

        public static SaveLineCommand toCommand(HmeFreezeDocLine dto) {
            SaveLineCommand co = new SaveLineCommand();
            BeanCopierUtil.copy(dto, co);
            return co;
        }

        public static MtMaterialLotVO20 toMaterialLot(SaveLineCommand dto) {
            MtMaterialLotVO20 sn = new MtMaterialLotVO20();
            sn.setMaterialLotId(dto.getMaterialLotId());
            sn.setFreezeFlag(YES);
            return sn;
        }
    }
}
