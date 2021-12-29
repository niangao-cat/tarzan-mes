package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeEquipmentMonitorVO
 *
 * @author chaonan.hu@hand-china.com 2020/07/16 18:51:04
 */
@Data
public class HmeEquipmentMonitorVO implements Serializable {
    private static final long serialVersionUID = 6640285496045654865L;

    @ApiModelProperty(value = "部门id")
    private String departmentId;
    @ApiModelProperty(value = "描述")
    private String departmentName;
    @ApiModelProperty(value = "类型")
    private String departmentCategory;
    @ApiModelProperty(value = "编码")
    private String departmentCode;
}
