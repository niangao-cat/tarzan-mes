package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * 拦截对象返回数据
 *
 * @author wengang.qiang@hand-china.com 2021/09/08 11:46
 */
@Data
public class HmeInterceptObjectVO implements Serializable {

    private static final long serialVersionUID = 8623589539263141985L;

    @ApiModelProperty(value = "拦截对象表主键")
    private String interceptObjectId;
    @ApiModelProperty(value = "物料id")
    private String materialId;
    @ApiModelProperty(value = "物料code")
    private String materialCode;
    @ApiModelProperty(value = "拦截对象")
    private String interceptObject;
    @ApiModelProperty(value = "状态")
    @LovValue(lovCode = "HME.INTERCEPT_STATUS", meaningField = "statusMeaning")
    private String status;
    @ApiModelProperty(value = "放行人姓名")
    private String releaseByName;
    @ApiModelProperty(value = "放行人id")
    private Long releaseBy;
    @ApiModelProperty(value = "状态描述")
    private String statusMeaning;
    @ApiModelProperty(value = "物料类型")
    private String materialType;
}
