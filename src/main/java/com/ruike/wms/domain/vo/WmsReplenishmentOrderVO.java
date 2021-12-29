package com.ruike.wms.domain.vo;

import com.ruike.qms.api.dto.QmsInvoiceHeadReturnDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/10/26 14:27
 */
@Data
public class WmsReplenishmentOrderVO implements Serializable {

    @ApiModelProperty(value = "头信息")
    private QmsInvoiceHeadReturnDTO qmsInvoiceHeadReturnDTO;

    @ApiModelProperty(value = "行信息")
    private List<WmsReplenishmentOrderLineVO> lineVOList;
}
