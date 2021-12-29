package com.ruike.wms.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.ruike.wms.domain.entity.WmsDistributionDemandDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WmsDistributionDemandDetailVO extends WmsDistributionDemandDetail implements Serializable {
    private static final long serialVersionUID = -6228043909291790694L;
    @ApiModelProperty(value = "工单号")
    private String workOrderNum;

    @ApiModelProperty(value="需求数量")
    private BigDecimal requirementQty;

    @ApiModelProperty(value="是否全局替代")
    private String enableReplaceFlag;
}
