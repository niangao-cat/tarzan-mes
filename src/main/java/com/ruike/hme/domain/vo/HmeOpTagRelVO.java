package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeOpTagRelVO
 * @Description 工艺采集项关系
 * @Date 2020/12/24 15:50
 * @Author yuchao.wang
 */
@Data
public class HmeOpTagRelVO implements Serializable {
    private static final long serialVersionUID = 7999785355737087355L;

    @ApiModelProperty("主键")
    private String opTagRelId;

    @ApiModelProperty(value = "工艺ID")
    private String operationId;

    @ApiModelProperty(value = "采集项ID")
    private String tagId;

    @ApiModelProperty(value = "采集项编码")
    private String tagCode;
}