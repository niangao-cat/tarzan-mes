package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;

/**
 * @Classname HmeBomComponentVO
 * @Description HmeBomComponentVO
 * @Date 2020/11/18 11:02
 * @Author yuchao.wang
 */
@Data
public class HmeBomComponentVO implements Serializable {
    private static final long serialVersionUID = 616821412045388861L;

    @ApiModelProperty(value = "bomComponentId")
    private String bomComponentId;

    @ApiModelProperty(value = "bom组件需求数量")
    private Double bomComponentQty;

    @ApiModelProperty(value = "bom组件物料ID")
    private String bomComponentMaterialId;

    @ApiModelProperty(value = "bom组件行号")
    private Long bomComponentLineNumber;

    @ApiModelProperty(value = "工单组件总需求数量")
    private String woBomComponentDemandQty;

    @ApiModelProperty(value = "工单组件扩展属性 预留项目号")
    private String reserveNum;

    @ApiModelProperty(value = "工单组件扩展属性 装配件标识")
    private String virtualFlag;

    @ApiModelProperty(value = "物料站点扩展属性 是否投料校验,Y不校验")
    private String issuedFlag;

    @ApiModelProperty(value = "bom组件替代料对应的主料组件ID")
    private String mainBomComponentId;

    @ApiModelProperty(value = "bom组件物料组")
    private String itemGroup;
}