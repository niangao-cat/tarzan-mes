package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname HmeBomComponentVO3
 * @Description HmeBomComponentVO3
 * @Date 2020/11/18 11:02
 * @Author yuchao.wang
 */
@Data
public class HmeBomComponentVO3 implements Serializable {
    private static final long serialVersionUID = -2366490785784009344L;

    @ApiModelProperty(value = "bomComponentId")
    private String bomComponentId;

    @ApiModelProperty(value = "bom组件需求数量")
    private Double bomComponentQty;

    @ApiModelProperty(value = "bom组件物料ID")
    private String bomComponentMaterialId;

    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;

    @ApiModelProperty(value = "bom组件行号")
    private Long lineNumber;

    @ApiModelProperty(value = "工单组件扩展属性 预留项目号")
    private String reserveNum;
}