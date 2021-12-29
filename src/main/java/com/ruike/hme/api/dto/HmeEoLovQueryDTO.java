package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeEoLovQueryDTO
 * @Description EO LOV查询DTO
 * @Date 2020/9/1 14:14
 * @Author yuchao.wang
 */
@Data
public class HmeEoLovQueryDTO implements Serializable {
    private static final long serialVersionUID = 7131998509800891951L;

    @ApiModelProperty(value = "工单ID")
    private String workOrderId;

    @ApiModelProperty(value = "站点ID")
    private String siteId;

    @ApiModelProperty(value = "EO编码")
    private String eoNum;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "标识说明")
    private String identification;
}