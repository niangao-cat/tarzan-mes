package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeAfterSaleQuotationHeaderVO7
 *
 * @author: chaonan.hu@hand-china.com 2021/09/30 09:21
 **/
@Data
public class HmeAfterSaleQuotationHeaderVO9 implements Serializable {
    private static final long serialVersionUID = 362312498908691326L;

    @ApiModelProperty(value = "头数据")
    private HmeAfterSaleQuotationHeaderVO7 header;

    @ApiModelProperty(value = "行数据")
    private List<HmeAfterSaleQuotationHeaderVO8> item;

}
