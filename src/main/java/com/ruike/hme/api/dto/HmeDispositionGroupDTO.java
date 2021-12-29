package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description 处置方法组DTO
 *
 * @author quan.luo@hand-china.com 2020/11/24 13:58
 */
@Data
public class HmeDispositionGroupDTO implements Serializable {

    private static final long serialVersionUID = 7446902843805239963L;

    @ApiModelProperty(value = "组id")
    private String dispositionGroupId;

    @ApiModelProperty(value = "处置组编码")
    private String dispositionGroup;

    @ApiModelProperty(value = "处置组描述")
    private String description;

    @ApiModelProperty(value = "生产站点ID")
    private String siteId;

    @ApiModelProperty(value = "生产站点编码")
    private String siteCode;

    @ApiModelProperty(value = "生产站点描述")
    private String siteName;

}
