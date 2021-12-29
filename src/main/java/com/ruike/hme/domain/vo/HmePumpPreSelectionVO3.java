package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmePumpPreSelectionVO3
 *
 * @author: chaonan.hu@hand-china.com 2021/08/30 14:19:34
 **/
@Data
public class HmePumpPreSelectionVO3 implements Serializable {
    private static final long serialVersionUID = -2647501867643347022L;

    @ApiModelProperty(value = "容器数")
    private Long containerQty;

    @ApiModelProperty(value = "泵浦源数")
    private Long pumpQty;

    @ApiModelProperty(value = "默认存储库位Id")
    private String defaultStorageLocatorId;

    @ApiModelProperty(value = "泵浦源条码信息")
    private List<HmePumpPreSelectionVO4> pumpMaterialLotInfoList;
}
