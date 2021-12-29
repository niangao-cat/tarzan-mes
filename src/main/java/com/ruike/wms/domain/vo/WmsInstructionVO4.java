package com.ruike.wms.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class WmsInstructionVO4 implements Serializable {

    private static final long serialVersionUID = 5616981796188485982L;
    @ApiModelProperty(value = "单据头Id")
    private String instructionDocId;

    @ApiModelProperty(value = "单据行Id")
    private String instructionId;

}
