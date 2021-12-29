package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/1 15:13
 */
@Data
public class HmeLoadUntieImportVO implements Serializable {

    private static final long serialVersionUID = -7152270420539430515L;

    @ApiModelProperty("条码")
    private String materialLotCode;

    @ApiModelProperty("位置")
    private String position;

    @ApiModelProperty("列")
    private String loadColumn;

    @ApiModelProperty("行")
    private String loadRow;

    @ApiModelProperty("热沉")
    private String hotSinkCode;
}
