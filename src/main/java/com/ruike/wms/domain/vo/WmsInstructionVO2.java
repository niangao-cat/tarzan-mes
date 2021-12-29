package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class WmsInstructionVO2 implements Serializable {
    private static final long serialVersionUID = -5111496253709423093L;


    @ApiModelProperty(value = "来源仓库")
    private String fromLocatorId;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "物料id")
    private String materialId;

    @ApiModelProperty(value = "头表单据id")
    private String sourceDocId;

    @ApiModelProperty(value = "行id")
    private List<String> lineIdList;
}
