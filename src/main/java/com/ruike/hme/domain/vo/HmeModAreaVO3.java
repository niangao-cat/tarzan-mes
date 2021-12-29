package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 不良申请单审核（事业部LOV）
 *
 * @author sanfeng.zhang@hand-china.com 2020/12/24 17:26
 */
@Data
public class HmeModAreaVO3 implements Serializable {

    private static final long serialVersionUID = -4715546007431644647L;

    @ApiModelProperty(value = "事业部ID")
    private String areaId;
    @ApiModelProperty(value = "事业部编码")
    private String areaCode;
    @ApiModelProperty(value = "事业部名称")
    private String areaName;
    @ApiModelProperty(value = "默认标识 N-否 Y-是")
    private String defaultOrganizationFlag;
}
