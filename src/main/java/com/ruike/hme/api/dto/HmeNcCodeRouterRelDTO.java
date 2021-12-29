package com.ruike.hme.api.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author penglin.sui@hand-china.com 2021/3/30 16:11
 */
@Data
public class HmeNcCodeRouterRelDTO implements Serializable {
    private static final long serialVersionUID = -6053440056611402628L;

    @ApiModelProperty(value = "不良代码组ID")
    private String ncGroupId;

    @ApiModelProperty(value = "不良代码ID")
    private String ncCodeId;

    @ApiModelProperty(value = "产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "器件类型")
    private String deviceType;

    @ApiModelProperty(value = "芯片类型")
    private String chipType;

    @ApiModelProperty(value = "有效性")
    private String enableFlag;

    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;

    @ApiModelProperty(value = "工艺")
    private String operationId;

    @ApiModelProperty(value = "工艺集合")
    @JsonIgnore
    private List<String> operationIdList;

    public void initParam () {
        operationIdList = StringUtils.isNotBlank(operationId) ? Arrays.asList(StringUtils.split(operationId, ",")) : null;
    }
}
