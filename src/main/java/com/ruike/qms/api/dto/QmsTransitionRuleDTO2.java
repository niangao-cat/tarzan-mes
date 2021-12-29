package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 默认站点
 * @Author tong.li
 * @Date 2020/5/19 10:21
 * @Version 1.0
 */
@Data
public class QmsTransitionRuleDTO2 implements Serializable {
    private static final long serialVersionUID = -7261625207990573492L;

    @ApiModelProperty(value = "站点Id")
    private String siteId;

    @ApiModelProperty(value = "站点编码")
    private String siteCode;

    @ApiModelProperty(value = "站点名称")
    private String siteName;
}
