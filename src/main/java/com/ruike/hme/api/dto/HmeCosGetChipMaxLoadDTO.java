package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeCosGetChipSiteOutConfirmDTO
 * @Description COS取片平台-查询容器对应最大装载数量
 * @Date 2020/8/18 20:09
 * @Author yuchao.wang
 */
@Data
public class HmeCosGetChipMaxLoadDTO implements Serializable {
    private static final long serialVersionUID = 330754795046982359L;

    @ApiModelProperty("工艺路线ID")
    private String operationId;

    @ApiModelProperty("COS类型")
    private String cosType;

    @ApiModelProperty("容器类型")
    private String containerType;

    @ApiModelProperty("容器对应最大装载数量")
    private Long maxLoadNumber;
}