package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/24 20:03
 */
@Data
public class HmeUserOrganizationVO2 implements Serializable {

    private static final long serialVersionUID = -6969718370738574027L;

    @ApiModelProperty(value = "制造部id")
    private String areaId;

    @ApiModelProperty(value = "制造部编码")
    private String areaCode;

    @ApiModelProperty(value = "制造部名称")
    private String areaName;

    @ApiModelProperty(value = "默认组织标识 N-否 Y-是")
    private String defaultOrganizationFlag;
}
