package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeServiceSplitRecordVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.io.Serializable;

/**
 * 售后返品拆机
 *
 * @author jiangling.zheng@hand-china.com 2020-09-08 17:10:57
 */
@Data
public class HmeServiceSplitRecordDTO3 implements Serializable {

    private static final long serialVersionUID = 7362696386936130111L;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料类型")
    private String itemType;
    @ApiModelProperty(value = "物料组")
    private String itemGroupCode;
    @ApiModelProperty(value = "物料组说明")
    private String itemGroupDescription;
    @ApiModelProperty(value = "用户站点ID")
    private String siteId;
    @ApiModelProperty(value = "售后返品Id")
    private String splitRecordId;
    @ApiModelProperty(value = "产线ID")
    private String productionLineId;
    @ApiModelProperty(value = "是否维修")
    private String isRepair;
    @ApiModelProperty(value = "是否库存管理")
    private String isOnhand;
    @ApiModelProperty("工位ID")
    private String workcellId;
    @ApiModelProperty("工艺路线ID")
    private String operationId;
    @ApiModelProperty("班组ID")
    private String wkcShiftId;
    @ApiModelProperty("工单ID")
    private String workOrderId;
    @ApiModelProperty("货位ID")
    private String issuedLocatorId;
    @ApiModelProperty("工单编号")
    private String workOrderNum;
    @ApiModelProperty("外部订单号")
    private String internalOrderNum;
    @ApiModelProperty("物料单位ID")
    private String uomId;
    @ApiModelProperty("物料单位编码")
    private String uomCode;
    @ApiModelProperty("事件编码")
    private String eventCode;
    @ApiModelProperty("创建原因")
    private String createReason;
    @ApiModelProperty(value = "新增物料批", hidden = true)
    private MtMaterialLot newMaterialLot;
    @ApiModelProperty(value = "批次号", hidden = true)
    private String lotCode;
    @ApiModelProperty(value = "是否返修序列号", hidden = true)
    private Boolean repairSnFlag;
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    @ApiModelProperty("实物返回属性")
    private String backType;

    public HmeServiceSplitRecordVO toCreateCommand() {
        HmeServiceSplitRecordVO command = new HmeServiceSplitRecordVO();
        command.setSiteId(this.getSiteId());
        command.setWorkcellId(this.getWorkcellId());
        command.setOperationId(this.getOperationId());
        command.setWkcShiftId(this.getWkcShiftId());
        command.setSnNum(this.getMaterialLotCode());
        command.setBackType(this.getBackType());
        return command;
    }
}
