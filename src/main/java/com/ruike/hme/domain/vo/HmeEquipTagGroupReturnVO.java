package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeEqManageTagGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 设备管理项目组查询VO
 * @author: han.zhang
 * @create: 2020/06/10 15:19
 */
@Getter
@Setter
@ToString
public class HmeEquipTagGroupReturnVO extends HmeEqManageTagGroup implements Serializable {
    private static final long serialVersionUID = -125556742429244985L;
    @ApiModelProperty(value = "站点编码")
    private String siteCode;
    @ApiModelProperty(value = "站点描述")
    private String siteName;
    @ApiModelProperty(value = "部门id")
    private String businessId;
    @ApiModelProperty(value = "部门")
    private String businessName;
    @ApiModelProperty(value = "工艺id")
    private String operationId;
    @ApiModelProperty(value = "工艺编码")
    private String operationName;
    @ApiModelProperty(value = "工艺描述")
    private String operationDescription;
    @ApiModelProperty(value = "设备使用年限")
    @LovValue(value = "HME.SERVICE_LIFE", meaningField = "serviceLifeMeaning")
    private String serviceLife;
    @ApiModelProperty(value = "设备使用年限含义")
    private String serviceLifeMeaning;
    @ApiModelProperty(value = "设备组编码")
    private String tagGroupCode;
    @ApiModelProperty(value = "设备组描述")
    private String tagGroupDescription;
    @ApiModelProperty(value = "项目管理类型")
    @LovValue(value = "HME.EQUIPMENT_MANAGE_TYPE", meaningField = "manageTypeMeaning")
    private String manageType;
    @ApiModelProperty(value = "项目管理类型含义")
    private String manageTypeMeaning;
    @ApiModelProperty(value = "设备类别")
    @LovValue(value = "HME.EQUIPMENT_CATEGORY", meaningField = "equipmentCategoryMeaning")
    private String equipmentCategory;
    @ApiModelProperty(value = "设备类别含义")
    private String equipmentCategoryMeaning;
    @LovValue(value = "HME.EQUIPMENT_MANAGE_STATUS", meaningField = "statusMeaning")
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "状态含义")
    private String statusMeaning;
}