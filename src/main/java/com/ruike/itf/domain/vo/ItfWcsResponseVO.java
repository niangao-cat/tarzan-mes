package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ItfWcsResponseVO
 *
 * @author: chaonan.hu@hand-china.com 2021/07/14 15:01
 **/
@Data
public class ItfWcsResponseVO implements Serializable {
    private static final long serialVersionUID = -1625375124774789260L;

    @ApiModelProperty("header")
    private List<ItfMaterialLotConfirmIfaceVO2> header;

    @ApiModelProperty("body")
    private Object body;
}
