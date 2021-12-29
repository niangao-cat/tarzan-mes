package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 新增检验组参数
 * @author: han.zhang
 * @create: 2020/04/22 21:48
 */
@Getter
@Setter
@ToString
public class QmsAddTagGroupDTO implements Serializable {
    private static final long serialVersionUID = -6968065680311841507L;

    @ApiModelProperty(value = "物料检验项id",required = true)
    private String inspectionSchemeId;

    @ApiModelProperty(value = "检验组id",required = true)
    private String tagGroupId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "工序id")
    private String processId;
}