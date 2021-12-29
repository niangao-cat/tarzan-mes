package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 * 工装修改历史查询条件
 * @author li.zhang 2021/01/07 13:20
 */
@Data
public class HmeToolHisDTO implements Serializable {
    private static final long serialVersionUID = -72830785500751849L;

    @ApiModelProperty(value = "工装ID")
    private String toolId;

}
