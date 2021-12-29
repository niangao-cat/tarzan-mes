package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeProductionGroupDTO
 *
 * @author: chaonan.hu@hand-china.com 2021/05/27 13:56:12
 **/
@Data
public class HmeProductionGroupDTO implements Serializable {
    private static final long serialVersionUID = -1905307297723758503L;

    @ApiModelProperty(value = "主键,传则更新，不传则新增")
    private String productionGroupId;

    @ApiModelProperty(value = "站点ID", required = true)
    private String siteId;

    @ApiModelProperty(value = "产品组编码", required = true)
    private String productionGroupCode;

    @ApiModelProperty(value = "产品组名称")
    private String description;

    @ApiModelProperty(value = "有效性", required = true)
    private String enableFlag;
}
