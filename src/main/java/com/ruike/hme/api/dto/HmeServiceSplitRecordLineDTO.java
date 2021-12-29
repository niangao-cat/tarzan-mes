package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.Date;

/**
 * 售后返品拆机行信息
 *
 * @author jiangling.zheng@hand-china.com 2020-09-08 17:10:57
 */
@Data
public class HmeServiceSplitRecordLineDTO implements Serializable {

    private static final long serialVersionUID = 7362696386936130111L;

    @ApiModelProperty(value = "售后返品Id")
    private String splitRecordId;
    @ApiModelProperty(value = "SN编码")
    private String snNum;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "物料组")
    private String itemGroupCode;
    @ApiModelProperty(value = "物料组说明")
    private String itemGroupDescription;
    @ApiModelProperty(value = "工单id")
    private String workOrderId;
    @ApiModelProperty(value = "工单编码")
    private String workOrderNum;
    @ApiModelProperty(value = "是否维修")
    @LovValue(value = "Z.FLAG_YN", meaningField = "isRepairMeaning")
    private String isRepair;
    @ApiModelProperty(value = "是否维修说明")
    private String isRepairMeaning;
    @ApiModelProperty(value = "是否库存管理")
    @LovValue(value = "Z.FLAG_YN", meaningField = "isOnhandMeaning")
    private String isOnhand;
    @ApiModelProperty(value = "是否库存管理说明")
    private String isOnhandMeaning;
    @ApiModelProperty(value = "状态")
    @LovValue(value = "HME.SPLIT_STATUS", meaningField = "splitStatusMeaning")
    private String splitStatus;
    @ApiModelProperty(value = "状态说明")
    private String splitStatusMeaning;
    @ApiModelProperty(value = "拆机人")
    private Long splitBy;
    @ApiModelProperty(value = "拆机人名称")
    private String splitByName;
    @ApiModelProperty(value = "返回属性")
    private Date splitTime;
    @ApiModelProperty(value = "备注")
    private String remark;
}
