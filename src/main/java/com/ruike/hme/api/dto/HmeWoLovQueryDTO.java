package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeWoLovQueryDTO
 * @Description 工单LOV查询DTO
 * @Date 2020/9/1 14:14
 * @Author yuchao.wang
 */
@Data
public class HmeWoLovQueryDTO implements Serializable {
    private static final long serialVersionUID = -947315104974378056L;

    @ApiModelProperty(value = "工位ID")
    private String workcellId;

    @ApiModelProperty(value = "产线ID")
    private String productionLineId;

    @ApiModelProperty(value = "工单编码")
    private String workOrderNum;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;
}