package com.ruike.hme.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeAfterSaleQuotationHeaderVO13
 *
 * @author: chaonan.hu@hand-china.com 2021/09/31 09:25
 **/
@Data
public class HmeAfterSaleQuotationHeaderVO13 implements Serializable {
    private static final long serialVersionUID = -1195973115244665469L;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "消息")
    private String message;
}
