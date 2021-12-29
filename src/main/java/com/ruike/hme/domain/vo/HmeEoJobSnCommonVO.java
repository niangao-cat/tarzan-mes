package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/8 14:35
 */
@Data
public class HmeEoJobSnCommonVO implements Serializable {

    private static final long serialVersionUID = 5126050215379820563L;

    @ApiModelProperty("拦截单ID")
    private String interceptId;
    @ApiModelProperty("拦截单")
    private String interceptNum;
    @ApiModelProperty("拦截维度")
    private String dimension;
    @ApiModelProperty("拦截消息")
    private String remark;
}
