package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 二维码解析
 *
 * @author jiangling.zheng@hand-china.com 2020-10-08 14:19
 */
@Data
public class WmsQrCodeAnalysisDTO2 implements Serializable {


    private static final long serialVersionUID = 5485342753512414673L;
    @ApiModelProperty(value = "二维码")
    private String barCode;

}
