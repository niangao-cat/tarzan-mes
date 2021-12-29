package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description
 *
 * @author li.zhang 2021/07/30 10:24
 */
@Data
public class WmsInstructionSnRelVO implements Serializable {

    private static final long serialVersionUID = 8866407399858558416L;

    @ApiModelProperty(value = "物料批Id")
    private String materialLotId;
    @ApiModelProperty(value = "单据行Id")
    private String instructionId;
}
