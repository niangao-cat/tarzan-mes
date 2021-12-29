package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruike.hme.domain.entity.HmeWipStocktakeRange;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * HmeWipStocktakeDocVO5
 *
 * @author: chaonan.hu@hand-china.com 2021/3/8 16:16:23
 **/
@Data
public class HmeWipStocktakeDocVO5 implements Serializable {
    private static final long serialVersionUID = -1265292009835557109L;

    @ApiModelProperty("条码ID")
    private String materialLotId;

    @ApiModelProperty("物料组")
    private String itemGroup;

    @ApiModelProperty("产线ID")
    private String prodLineId;

    @ApiModelProperty("工序ID")
    private String workcellId;

    @ApiModelProperty("LOT")
    private String lot;

    @ApiModelProperty("物料ID")
    private String materialId;

    private String currentContainerId;

    private String primaryUomId;

    private String primaryUomQty;

    private String workOrderId;

    @ApiModelProperty(value = "在制ID")
    @JsonIgnore
    private String eoStepWipId;
    @ApiModelProperty(value = "更新时间")
    @JsonIgnore
    private String lastUpdateDate;

    private String eoId;
}
