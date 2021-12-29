package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description 处置方法DTO
 *
 * @author quan.luo@hand-china.com 2020/11/24 10:04
 */
@Data
public class HmeDispositionFunctionDTO implements Serializable {

    private static final long serialVersionUID = 748142223134827127L;

    @ApiModelProperty(value = "处置方法id")
    private String dispositionFunctionId;

    @ApiModelProperty(value = "生产站点")
    private String siteId;

    @ApiModelProperty(value = "生产站点编码")
    private String siteCode;

    @ApiModelProperty(value = "生产站点描述")
    private String siteName;

    @ApiModelProperty(value = "处置方法编码")
    private String dispositionFunction;

    @ApiModelProperty(value = "处置方法描述")
    private String description;

    @ApiModelProperty(value = "工艺路线id")
    private String routerId;

    @ApiModelProperty(value = "工艺路线编码")
    private String routerName;

    @ApiModelProperty(value = "工艺类型描述")
    private String routerDescription;

    @ApiModelProperty(value = "方法类型")
    private String functionType;

    @ApiModelProperty(value = "方法类型描述")
    private String functionTypeDescription;

    @ApiModelProperty(value = "序号")
    private Long sequence;

    @ApiModelProperty(value = "方法与组关系id")
    private String dispositionGroupMemberId;
}
