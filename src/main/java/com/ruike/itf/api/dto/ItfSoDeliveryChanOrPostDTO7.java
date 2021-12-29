package com.ruike.itf.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/14 19:05
 */
@Data
public class ItfSoDeliveryChanOrPostDTO7 implements Serializable {

    private static final long serialVersionUID = -8329082592273613192L;

    @ApiModelProperty("单据号")
    @JSONField(name = "VBELN")
    private String VBELN;
    @ApiModelProperty("头取消标识")
    @JSONField(name = "H_CANCEL")
    private String H_CANCEL;
}
