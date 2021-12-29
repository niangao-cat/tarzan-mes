package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/6 18:39
 */
@Data
public class ItfStocktakeVO4 implements Serializable {

    private static final long serialVersionUID = 3471687898673357139L;

    @ApiModelProperty("资产编码")
    private String assetEncoding;
    @ApiModelProperty("盘点单")
    private String stocktakeNum;
    @ApiModelProperty("租户ID")
    private Long tenantId;
    @ApiModelProperty("处理标识（成功S/失败E）")
    private String processFlag;
    @ApiModelProperty("处理消息")
    private String processMessage;
}
