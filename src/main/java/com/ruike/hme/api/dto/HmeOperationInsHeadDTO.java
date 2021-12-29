package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-11-09 20:45
 */
@Data
public class HmeOperationInsHeadDTO implements Serializable {


    private static final long serialVersionUID = 7695461887008130772L;

    @ApiModelProperty(value = "头ID")
    private String insHeadId;
    @ApiModelProperty(value = "文件编码")
    private String attachmentCode;
    @ApiModelProperty(value = "文件名称")
    private String attachmentName;
}
