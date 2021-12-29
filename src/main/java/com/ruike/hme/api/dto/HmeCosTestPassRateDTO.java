package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * COS测试良率维护表查询条件
 *
 * @author wengang.qiang@hand-china.com 2021/09/06 14:18
 */
@Data
public class HmeCosTestPassRateDTO implements Serializable {

    private static final long serialVersionUID = 483149742915619814L;

    @ApiModelProperty(value = "cos类型")
    private String cosType;
    @ApiModelProperty(value = "是否有效")
    private String enableFlag;
}
