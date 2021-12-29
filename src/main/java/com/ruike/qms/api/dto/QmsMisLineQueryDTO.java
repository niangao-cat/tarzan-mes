package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 行查询DTO
 * @author: han.zhang
 * @create: 2020/04/22 17:16
 */
@Getter
@Setter
@ToString
public class QmsMisLineQueryDTO implements Serializable {
    private static final long serialVersionUID = -2293552864317716428L;

    @ApiModelProperty(value = "物料检验计划id",required = true)
    private String inspectionSchemeId;

    @ApiModelProperty(value = "检验组id")
    private String tagGroupId;
}