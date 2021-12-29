package com.ruike.wms.domain.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.instruction.domain.entity.MtInstructionDoc;

import java.io.Serializable;
import java.util.Date;

@Data
public class WmsInstructionDocVO extends MtInstructionDoc implements Serializable {

    private static final long serialVersionUID = 6882476193665765167L;

    @ApiModelProperty(value = "创建时间从")
    private Date creationDateFrom;

    @ApiModelProperty(value = "创建时间至")
    private Date creationDateTo;
}
