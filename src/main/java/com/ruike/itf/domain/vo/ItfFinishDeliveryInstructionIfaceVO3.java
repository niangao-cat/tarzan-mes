package com.ruike.itf.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/19 1:17
 */
@Data
public class ItfFinishDeliveryInstructionIfaceVO3 implements Serializable {

    private static final long serialVersionUID = -9137427094820482756L;

    @ApiModelProperty("header")
    private List<ItfFinishDeliveryInstructionIfaceVO4> header;

    @ApiModelProperty("body")
    private Object body;

}
