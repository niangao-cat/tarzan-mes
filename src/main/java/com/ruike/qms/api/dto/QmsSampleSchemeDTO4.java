package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * QmsSampleSchemeDTO4
 *
 * @author: chaonan.hu@hand-china.com 2020-10-15 20:29
 **/
@Data
public class QmsSampleSchemeDTO4 implements Serializable {
    private static final long serialVersionUID = -4458881094809687226L;

    @ApiModelProperty(value = "主键")
    private String schemeId;

    @ApiModelProperty(value = "样本量字码")
    private String sampleSizeCodeLetter;

    @ApiModelProperty(value = "ACL值")
    private String acceptanceQuantityLimit;
}
