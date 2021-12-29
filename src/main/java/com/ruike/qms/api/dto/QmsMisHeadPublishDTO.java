package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 物料检验计划发布DTO
 * @author: han.zhang
 * @create: 2020/04/22 16:12
 */
@Getter
@Setter
@ToString
public class QmsMisHeadPublishDTO implements Serializable {

    private static final long serialVersionUID = 5901706392333574232L;

    @ApiModelProperty(value = "检验计划id")
    private String inspectionSchemeId;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "是否发布")
    private String publishFlag;
}