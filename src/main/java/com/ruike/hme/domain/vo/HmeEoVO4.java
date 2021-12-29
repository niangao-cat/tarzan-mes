package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.order.domain.entity.MtEo;

import java.util.*;

/**
 * @Classname HmeEoVO4
 * @Description EO视图
 * @Date 2020/11/5 16:51
 * @Author yuchao.wang
 */
@Data
public class HmeEoVO4 extends MtEo {
    private static final long serialVersionUID = -3643153869570714446L;

    @ApiModelProperty(value = "物料类型")
    private String itemType;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "单位编码")
    private String uomCode;

    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "mt_bom_component_attr lineAttribute24")
    private String relatedLineNum;

    @ApiModelProperty(value = "routerId")
    private String routerId;

    @ApiModelProperty(value = "bomId")
    private String bomId;

    @ApiModelProperty(value = "bom需求数量")
    private Double bomPrimaryQty;

    @ApiModelProperty(value = "bom组件信息")
    private List<HmeBomComponentVO> bomComponentList;
}