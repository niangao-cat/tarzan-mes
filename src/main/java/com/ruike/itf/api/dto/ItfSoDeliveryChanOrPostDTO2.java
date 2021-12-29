package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/12 15:33
 */
@Data
public class ItfSoDeliveryChanOrPostDTO2 implements Serializable {

    private static final long serialVersionUID = 4463444041440851243L;
    @ApiModelProperty("单据id")
    private String instructionDocId;
    @ApiModelProperty("单据编码")
    private String instructionDocNum;
    @ApiModelProperty("站点id")
    private String siteId;
    @ApiModelProperty("站点编码")
    private String siteCode;
}
