package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备点检
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */
@Data
public class HmeEqTaskDocEquipmentDTO implements Serializable {

    private static final long serialVersionUID = -2950697706441942843L;

    @ApiModelProperty(value = "项目组ID")
    private String manageTagGroupId;
    @ApiModelProperty(value = "设备类别")
    private String equipmentCategory;
    @ApiModelProperty(value = "设备ID")
    private String equipmentId;
    @ApiModelProperty(value = "管理周期")
    private String manageCycle;
    @ApiModelProperty(value = "站点")
    private String siteId;
    @ApiModelProperty(value = "层级")
    private Integer eqLevel;
    @ApiModelProperty(value = "单据类型")
    private String docType;

}
