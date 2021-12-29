package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmePumpPreSelectionVO15
 *
 * @author: chaonan.hu@hand-china.com 2021/09/06 13:59:34
 **/
@Data
public class HmePumpPreSelectionVO15 implements Serializable {
    private static final long serialVersionUID = -5207489083403205491L;

    @ApiModelProperty(value = "筛选批次")
    private String selectionLot;

    @ApiModelProperty(value = "套数")
    private Integer setsNum;

    @ApiModelProperty(value = "泵浦源条码信息")
    private List<HmePumpPreSelectionVO4> pumpMaterialLotInfoList;
}
