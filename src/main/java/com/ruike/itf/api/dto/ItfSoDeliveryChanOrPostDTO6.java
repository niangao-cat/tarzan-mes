package com.ruike.itf.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/13 11:03
 */
@Data
public class ItfSoDeliveryChanOrPostDTO6 implements Serializable {

    private static final long serialVersionUID = 7860015774167636088L;

    @ApiModelProperty("单据号")
    @JSONField(name = "VBELN")
    private String VBELN;
    @ApiModelProperty("行号")
    @JSONField(name = "POSNR")
    private String POSNR;
    @ApiModelProperty("序列号")
    @JSONField(name = "SERNR")
    private String SERNR;

}
