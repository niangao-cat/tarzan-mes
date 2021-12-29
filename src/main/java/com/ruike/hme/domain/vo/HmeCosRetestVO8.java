package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/24 23:58
 */
@Data
public class HmeCosRetestVO8 implements Serializable {

    private static final long serialVersionUID = 698361555000310931L;

    @ApiModelProperty(value = "来料条码Id")
    private String sourceMaterialLotId;

    @ApiModelProperty(value = "来料条码")
    private String sourceMaterialLotCode;

    @ApiModelProperty(value = "数量")
    private Double primaryUomQty;

    @ApiModelProperty(value = "条码清单")
    private List<HmeCosRetestVO10> returnMaterialLotList;

    @ApiModelProperty(value = "投料条码清单")
    private List<HmeCosRetestVO10> feelMaterialLotList;

}
