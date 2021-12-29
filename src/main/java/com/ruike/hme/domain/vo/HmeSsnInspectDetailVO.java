package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/02/01 17:18
 */
@Data
public class HmeSsnInspectDetailVO implements Serializable {

    private static final long serialVersionUID = 515067479012571602L;

    @ApiModelProperty("行表ID")
    private String ssnInspectLineId;

    @ApiModelProperty("行明细表ID")
    private String ssnInspectDetailId;

    @ApiModelProperty("数据项收集组ID")
    private String tagGroupId;

    @ApiModelProperty("数据项收集组编码")
    private String tagGroupCode;

    @ApiModelProperty("数据项收集组描述")
    private String tagGroupDescription;

}
