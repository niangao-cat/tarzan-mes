package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeAfterSaleQuotationHeaderVO4
 *
 * @author: chaonan.hu@hand-china.com 2021/09/26 16:02
 **/
@Data
public class HmeAfterSaleQuotationHeaderVO4 implements Serializable {
    private static final long serialVersionUID = 8625950713317108544L;

    @ApiModelProperty(value = "行主键")
    private String quotationLineId;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "数量")
    private BigDecimal requsetQty;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "质保内发货日期")
    private Date sendDate;

    @ApiModelProperty(value = "情况说明")
    private String situationDesc;
}
