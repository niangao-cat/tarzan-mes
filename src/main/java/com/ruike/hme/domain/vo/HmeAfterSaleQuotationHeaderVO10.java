package com.ruike.hme.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeAfterSaleQuotationHeaderVO10
 *
 * @author: chaonan.hu@hand-china.com 2021/09/30 09:21
 **/
@Data
public class HmeAfterSaleQuotationHeaderVO10 implements Serializable {
    private static final long serialVersionUID = -5044001243269640054L;

    @ApiModelProperty(value = "报价单号")
    @JSONField(name = "QUOTATION_NO")
    private String QUOTATION_NO;

    @ApiModelProperty(value = "类型")
    @JSONField(name = "TYPE")
    private String TYPE;

    @ApiModelProperty(value = "消息")
    @JSONField(name = "MESSAGE")
    private String MESSAGE;
}
