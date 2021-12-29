package com.ruike.hme.domain.vo;

import java.io.Serializable;

import com.ruike.hme.domain.entity.HmeNcCodeRouterRel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author penglin.sui@hand-china.com 2021/3/30 16:25
 */

@Data
public class HmeNcCodeRouterRelVO extends HmeNcCodeRouterRel implements Serializable {
    private static final long serialVersionUID = -5623319262937088353L;

    @ApiModelProperty(value = "不良代码组编码")
    private String ncGroupCode;

    @ApiModelProperty(value = "不良代码组描述")
    private String ncGroupDescription;

    @ApiModelProperty(value = "不良代码编码")
    private String ncCode;

    @ApiModelProperty(value = "不良代码描述")
    private String ncCodeDescription;

    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "产线描述")
    private String prodLineDescription;

    @ApiModelProperty(value = "工艺路线编码")
    private String routerName;

    @ApiModelProperty(value = "工艺路线描述")
    private String routerDesc;

    @ApiModelProperty(value = "工艺路线版本")
    private String routerVersion;

    @ApiModelProperty(value = "工艺名称")
    private String operationName;

    @ApiModelProperty(value = "工艺描述")
    private String operationDescription;

    @ApiModelProperty(value = "工艺")
    private String operationId;
}
