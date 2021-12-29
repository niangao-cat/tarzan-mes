package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/6 18:35
 */
@Data
public class ItfStocktakeVO3 implements Serializable {

    private static final long serialVersionUID = -2718704935615359367L;

    @ApiModelProperty("租户")
    private Long tenantId;
    @ApiModelProperty("盘点单")
    private String stocktakeNum;
    @ApiModelProperty("处理标识（成功S/失败E）")
    private String processFlag;
    @ApiModelProperty("处理消息")
    private String processMessage;
    @ApiModelProperty("设备信息")
    private List<ItfStocktakeVO4> equipmentList;
}
