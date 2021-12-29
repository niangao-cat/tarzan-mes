package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * CosQueryCollectItfReturnDTO4
 *
 * @author: chaonan.hu@hand-china.com 2021-10-11 10:40:43
 **/
@Data
public class CosQueryCollectItfReturnDTO4 implements Serializable {
    private static final long serialVersionUID = -8404747958899259434L;

    @ApiModelProperty(value = "偏振度和发散角测试结果表主键")
    private String degreeTestId;

    @ApiModelProperty(value = "测试对象")
    private String testObject;

    @ApiModelProperty(value = "测试数量")
    private Long testQty;

    @ApiModelProperty(value = "测试类型")
    private String testType;

    @ApiModelProperty(value = "目标数量")
    private Long targetQty;
}
