package com.ruike.hme.api.dto.command;

import com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeActualRepresentation;
import com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeDocRepresentation;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.repository.HmeEquipmentRepository;
import com.ruike.hme.domain.repository.HmeEquipmentStocktakeActualRepository;
import com.ruike.hme.domain.repository.HmeEquipmentStocktakeDocRepository;
import com.ruike.wms.infra.util.StringCommonUtils;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.java.Log;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.EquipmentStatus.BF;
import static com.ruike.hme.infra.constant.HmeConstants.EquipmentStatus.CS;
import static com.ruike.hme.infra.constant.HmeConstants.EquipmentStocktakeStatus.DONE;
import static com.ruike.hme.infra.constant.HmeConstants.EquipmentStocktakeType.ALL;
import static com.ruike.hme.infra.constant.HmeConstants.EquipmentStocktakeType.RANDOM;

/**
 * <p>
 * 设备盘点单 创建命令
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 11:01
 */
@Data
@Log
public class HmeEquipmentStocktakeDocCreateCommand implements Serializable {
    private static final long serialVersionUID = -6320290140218216409L;

    @ApiModelProperty(value = "盘点单ID")
    private String stocktakeId;
    @ApiModelProperty(value = "盘点状态")
    @JsonIgnore
    private String stocktakeStatus;
    @ApiModelProperty(value = "盘点类型", required = true)
    @NotBlank
    private String stocktakeType;
    @ApiModelProperty(value = "盘点范围")
    private Integer stocktakeRange;
    @ApiModelProperty(value = "范围内数量", hidden = true)
    @JsonIgnore
    private Integer stocktakeRangeNum;
    @ApiModelProperty(value = "保管部门ID")
    private String businessId;
    @ApiModelProperty(value = "入账日期从")
    private Date postingDateFrom;
    @ApiModelProperty(value = "入账日期至")
    private Date postingDateTo;
    @ApiModelProperty(value = "台账类型")
    private String ledgerType;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty("租户")
    private Long tenantId;
    @ApiModelProperty(value = "忽略标志", hidden = true)
    @JsonIgnore
    private Boolean ignoreFlag;

    @ApiModelProperty("设备台账列表")
    private List<HmeEquipment> equipmentList;


    public void validObject(HmeEquipmentStocktakeDocRepository stocktakeDocRepository, HmeEquipmentStocktakeActualRepository stocktakeActualRepository, HmeEquipmentRepository equipmentRepository) {
        // 若传入单据号则认为是对原单据做更新，校验单据状态
        List<String> equipmentIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(this.stocktakeId)) {
            HmeEquipmentStocktakeDocRepresentation representation = stocktakeDocRepository.byId(this.stocktakeId, this.tenantId);
            WmsCommonUtils.processValidateMessage(this.tenantId, DONE.equals(representation.getStocktakeStatus()), "HME_EQ_STOCKTAKE_001", "HME");

            WmsCommonUtils.processValidateMessage(this.tenantId, ALL.equals(representation.getStocktakeType()), "HME_EQ_STOCKTAKE_002", "HME");

            // 查询盘点单的设备信息
            equipmentIdList = stocktakeActualRepository.queryStocktakeEquipment(tenantId, this.stocktakeId);
        }

        if (ALL.equals(this.stocktakeType)) {
            this.equipmentList = equipmentRepository.selectAll();
        }
        List<String> finalEquipmentIdList = equipmentIdList;
        // 20210802 add by sanfeng.zhang for peng.zhao 筛选掉判断盘点单已存在的
        this.equipmentList = this.equipmentList.stream().filter(eq -> !finalEquipmentIdList.contains(eq.getEquipmentId())).collect(Collectors.toList());

        // 筛选掉报废和出售的设备
        this.equipmentList = this.equipmentList.stream().filter(rec -> !StringCommonUtils.contains(rec.getEquipmentStatus(), BF, CS)).collect(Collectors.toList());

        // 对于RANDOM类型，按照盘点范围四舍五入计算总数，排除已盘设备，剩下的做处理
        if (RANDOM.equals(this.stocktakeType)) {
            // 乱序之后按数量截取
            Collections.shuffle(this.equipmentList);
            long prepareCount = Math.round((double) (equipmentList.size() * this.stocktakeRange) / 100D);
            log.info("==============prepareCount=>" + prepareCount);
            this.equipmentList = this.equipmentList.stream().limit(prepareCount).collect(Collectors.toList());
        }

        // 历史单据校验
        if (StringUtils.isNotBlank(this.stocktakeId)) {
            List<HmeEquipmentStocktakeActualRepresentation> dbActualList = stocktakeActualRepository.list(this.stocktakeId);
            Set<String> dbEquipmentIds = dbActualList.stream().map(HmeEquipmentStocktakeActualRepresentation::getEquipmentId).collect(Collectors.toSet());
            this.equipmentList = this.equipmentList.stream().filter(rec -> !dbEquipmentIds.contains(rec.getEquipmentId())).collect(Collectors.toList());
        }

        // 若筛选后设备为空设置忽略
        this.setIgnoreFlag(CollectionUtils.isEmpty(this.equipmentList));

        // 20210802 add by sanfeng.zhang for peng.zhao 保管部门取设备的
        String businessId = "";
        if (CollectionUtils.isNotEmpty(this.equipmentList)) {
            List<String> businessIdList = this.equipmentList.stream().map(HmeEquipment::getBusinessId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            businessId = StringUtils.join(businessIdList, ",");
        }
        this.businessId = businessId;
    }
}
