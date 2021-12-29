package com.ruike.hme.api.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yaqiong.zhou@raycus.com 2021/9/7 10:07
 */
@Data
public class HmeRepairLimitCountDTO implements Serializable {
    private static final long serialVersionUID = -5282959098250144632L;

    @ApiModelProperty(value = "部门")
    private String departmentId;
    @ApiModelProperty(value = "租户Id")
    private Long tenantId;
    @ApiModelProperty(value = "主键Id")
    private String repairLimitCountId;
    @ApiModelProperty(value = "物料Id")
    private String materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "工序Id")
    private String workcellId;
    @ApiModelProperty("工序编码")
    private String workcellCode;
    @ApiModelProperty(value = "限制次数")
    private String limitCount;
    @ApiModelProperty(value = "有效性")
    private String enableFlag;
}
