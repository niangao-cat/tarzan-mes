package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: tarzan-mes
 * @description: 设备管理项保存
 * @author: han.zhang
 * @create: 2020/06/11 15:39
 */
@Getter
@Setter
@ToString
public class HmeEquipManageTagSaveVO {
    @ApiModelProperty(value = "设备管理项id")
    private String manageTagId;

    @ApiModelProperty(value = "点检标识")
    private String checkFlag;
    @ApiModelProperty(value = "保养标识")
    private String maintainFlag;
    @ApiModelProperty(value = "点检周期")
    private String checkCycle;

    @ApiModelProperty(value = "保养周期")
    private String maintainCycle;
    @ApiModelProperty(value = "保养提前期")
    private String maintainLeadtime;

}