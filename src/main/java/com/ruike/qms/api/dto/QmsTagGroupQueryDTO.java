package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 检验组查询参数
 * @author: han.zhang
 * @create: 2020/04/22 21:33
 */
@Getter
@Setter
@ToString
public class QmsTagGroupQueryDTO implements Serializable {
    private static final long serialVersionUID = 1529817867021446461L;

    @ApiModelProperty(value = "物料检验项id",required = true)
    private String inspectionSchemeId;

    @ApiModelProperty(value = "检验组编码")
    private String tagGroupCode;
}