package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 头数据查询参数
 * @author: han.zhang
 * @create: 2020/04/22 09:48
 */
@Getter
@Setter
@ToString
public class QmsMaterialInspectionSchemeHeadDTO implements Serializable {

    private static final long serialVersionUID = -692813950009576931L;

    @ApiModelProperty(value = "组织")
    private String siteId;

    @ApiModelProperty(value = "物料")
    private String materialId;

    @ApiModelProperty(value = "检验类型")
    private String inspectionType;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;
}