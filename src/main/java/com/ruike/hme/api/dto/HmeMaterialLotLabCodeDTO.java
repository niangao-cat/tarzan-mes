package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeMaterialLotLabCodeDTO
 *
 * @author: chaonan.hu@hand-china.com 2021/11/03 16:05:24
 **/
@Data
public class HmeMaterialLotLabCodeDTO implements Serializable {
    private static final long serialVersionUID = 2242716010224298660L;

    @ApiModelProperty(value = "实物条码")
    private String materialLotCode;

    @ApiModelProperty(value = "实验代码")
    private String labCode;

    @ApiModelProperty(value = "实验备注")
    private String labRemark;
}
