package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/08/11 13:42
 */
@Data
public class ItfProductionPickingIfaceDTO implements Serializable {

    private static final long serialVersionUID = -3939036500053300190L;

    @ApiModelProperty(value = "指令单据id")
    private String instructionDocId;
    @ApiModelProperty(value = "指令id")
    private String instructionId;
    @ApiModelProperty(value = "实绩数量")
    private Double actualQty;

}
