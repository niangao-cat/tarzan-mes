package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 作者：ruijie.wang01@hand-china.com
 * 时间：2020/8/17 13:47
 */
@Data
public class HmeManageTagDTO implements Serializable {

    private static final long serialVersionUID = 1077163512955715670L;

    @ApiModelProperty(value = "项目编码")
    private String tagCode;
    @ApiModelProperty(value = "项目描述")
    private String tagDescription;
    @ApiModelProperty(value = "")
    private String locale;
}
