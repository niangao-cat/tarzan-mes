package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * HmeCosDegreeTestActualDTO
 *
 * @author: chaonan.hu@hand-china.com 2021-11-12 15:02
 **/
@Data
public class HmeCosDegreeTestActualDTO implements Serializable {
    private static final long serialVersionUID = 8484966286475301661L;

    @ApiModelProperty("COS类型")
    private String cosType;

    @ApiModelProperty("WAFER")
    private String wafer;

    @ApiModelProperty("测试对象")
    private String testObject;

    @ApiModelProperty("测试状态")
    private String testStatus;

    @ApiModelProperty("放行时间从")
    private Date releaseDateFrom;

    @ApiModelProperty("放行时间至")
    private Date releaseDateTo;
}
