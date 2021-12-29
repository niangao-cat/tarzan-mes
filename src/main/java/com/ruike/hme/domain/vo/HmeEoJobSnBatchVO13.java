package com.ruike.hme.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnBatchVO13 implements Serializable {
    private static final long serialVersionUID = 3009706134194670073L;
    @ApiModelProperty("进站条码")
    private String snNum;
}
