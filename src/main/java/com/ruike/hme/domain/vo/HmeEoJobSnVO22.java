package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnVO22 implements Serializable {
    private static final long serialVersionUID = 8277166394109505979L;
    @ApiModelProperty("工单ID")
    String workOrderId;
    @ApiModelProperty("工位ID")
    String workcellId;
    @ApiModelProperty("站点ID")
    String siteId;
}
