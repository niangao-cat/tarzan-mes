package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 铭牌打印配置属性维护条件查询
 *
 * @author wengang.qiang@hand-china.com 2021/10/12 11:26
 */
@Data
public class HmeNameplatePrintRelHeaderDTO implements Serializable {

    private static final long serialVersionUID = 6896872552098875930L;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "内部识别码")
    private String identifyingCode;

    @ApiModelProperty(value = "有效性")
    private String enableFlag;

}
