package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ItfMaterialLotConfirmIfaceVO3
 *
 * @author: chaonan.hu@hand-china.com 2021/07/14 15:50
 **/
@Data
public class ItfMaterialLotConfirmIfaceVO3 implements Serializable {
    private static final long serialVersionUID = 6417095547515442124L;

    @ApiModelProperty("条码类型")
    private String barcodeType;

    @ApiModelProperty("物料批ID集合")
    private List<String> materialLotIdList;
}
