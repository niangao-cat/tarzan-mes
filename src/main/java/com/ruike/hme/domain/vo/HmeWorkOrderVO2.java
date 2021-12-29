package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.order.domain.entity.MtWorkOrder;

import java.util.*;

/**
 * @Classname HmeWorkOrderVO2
 * @Description WO视图
 * @Date 2020/11/5 18:36
 * @Author yuchao.wang
 */
@Data
public class HmeWorkOrderVO2 extends MtWorkOrder {
    private static final long serialVersionUID = 8224873800502931562L;

    @ApiModelProperty(value = "扩展表属性attribute1 单据号")
    private String soNum;

    @ApiModelProperty(value = "扩展表属性attribute7 单据行号")
    private String soLineNum;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "产线编码")
    private String prodLineCode;

    @ApiModelProperty(value = "不投反冲料标识")
    private String backflushNotFlag;

    @ApiModelProperty(value = "是否投料校验,Y不校验")
    private String issuedFlag;

    @ApiModelProperty(value = "bom组件信息")
    private List<HmeBomComponentVO> bomComponentList;
}