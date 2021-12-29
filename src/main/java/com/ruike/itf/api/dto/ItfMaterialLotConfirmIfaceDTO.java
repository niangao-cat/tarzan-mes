package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ItfMaterialLotConfirmIfaceDTO
 *
 * @author: chaonan.hu@hand-china.com 2021/07/13 19:48
 **/
@Data
public class ItfMaterialLotConfirmIfaceDTO implements Serializable {
    private static final long serialVersionUID = 6983952585474815569L;

    @ApiModelProperty(value = "条码号或容器号集合")
    private List<String> barcodeList;
}
