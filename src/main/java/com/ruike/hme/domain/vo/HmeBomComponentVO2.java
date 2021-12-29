package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname HmeBomComponentVO2
 * @Description HmeBomComponentVO2
 * @Date 2020/11/18 11:02
 * @Author yuchao.wang
 */
@Data
public class HmeBomComponentVO2 implements Serializable {
    private static final long serialVersionUID = 4465163997992869975L;

    @ApiModelProperty(value = "bomComponentId")
    private String bomComponentId;

    @ApiModelProperty(value = "bom组件需求数量")
    private Double qty;

    @ApiModelProperty(value = "bom组件物料ID")
    private String materialId;

    @ApiModelProperty(value = "bom组件行号")
    private Long lineNumber;

    @ApiModelProperty(value = "工单组件扩展属性 装配件标识")
    private String virtualFlag;

    @ApiModelProperty(value = "bom组件物料编码")
    private String materialCode;

    @ApiModelProperty(value = "bom组件物料名称")
    private String materialName;

    @ApiModelProperty(value = "bom组件物料单位ID")
    private String primaryUomId;

    @ApiModelProperty(value = "bom组件物料单位类型")
    private String uomType;

    @ApiModelProperty(value = "bom组件物料站点信息")
    private String materialSiteId;

    @ApiModelProperty(value = "反冲料标识 2为反冲料")
    private String backFlashFlag;

    @ApiModelProperty(value = "时效物料对应的时效时长")
    private String availableTime;

    @ApiModelProperty(value = "物料类型 TIME:时效物料 SN:序列物料 LOT:批次物料")
    private String lotType;
}