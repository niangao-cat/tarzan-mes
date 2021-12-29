package com.ruike.itf.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/12 18:22
 */
@Data
public class ItfSoDeliveryChanOrPostDTO4 implements Serializable {

    private static final long serialVersionUID = 494678677647158560L;

    @ApiModelProperty("单据")
    @JSONField(name = "VBELN")
    private String VBELN;
    @ApiModelProperty("类型")
    @JSONField(name = "TYPE")
    private String TYPE;
    @ApiModelProperty("消息")
    @JSONField(name = "MESSAGE")
    private String MESSAGE;

    @JsonIgnore
    @ApiModelProperty("类型 CHANGE-修改、POST-过账")
    private String requestType;
}
