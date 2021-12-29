package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * 拦截工序返回数据
 *
 * @author wengang.qiang@hand-china.com 2021/09/07 18:57
 */
@Data
public class HmeInterceptWorkcellVO implements Serializable {

    private static final long serialVersionUID = -8817903738467786112L;

    @ApiModelProperty(value = "拦截工序主键")
    private String interceptWorkcellId;
    @ApiModelProperty(value = "拦截工序描述")
    private String workcellName;
    @ApiModelProperty(value = "工序code")
    private String workcellCode;
    @ApiModelProperty(value = "状态")
    @LovValue(lovCode = "HME.INTERCEPT_STATUS", meaningField = "statusMeaning")
    private String status;
    @ApiModelProperty(value = "放行人")
    private Long releaseBy;
    @ApiModelProperty(value = "放行人名称")
    private String releaseByName;
    @ApiModelProperty(value = "状态描述")
    private String statusMeaning;

}
