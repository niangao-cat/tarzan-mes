package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * 偏振度&发散角良率维护 头表查询条件
 *
 * @author wengang.qiang@hand-china 2021/09/14 11:01
 */
@Data
public class HmeTagPassRateHeaderDTO implements Serializable {

    private static final long serialVersionUID = 2809379041671408742L;

    @ApiModelProperty(value = "cos类型")
    private String cosType;

    @ApiModelProperty(value = "测试对象")
    private String testObject;

    @ApiModelProperty(value = "测试类型")
    private String testType;

    @ApiModelProperty(value = "有效性")
    private String enableFlag;
}
