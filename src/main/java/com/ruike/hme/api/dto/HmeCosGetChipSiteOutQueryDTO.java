package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeCosGetChipSiteOutQueryDTO
 * @Description COS取片平台-出站查询输入参数
 * @Date 2020/8/18 20:09
 * @Author yuchao.wang
 */
@Data
public class HmeCosGetChipSiteOutQueryDTO implements Serializable {

    private static final long serialVersionUID = -1893532317426380981L;

    @ApiModelProperty("工艺路线ID")
    private String operationId;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("设备ID")
    private String equipmentId;
}