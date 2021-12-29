package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 容器类型
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/18 10:49
 */
@Data
public class HmeChipTransferVO5 implements Serializable {

    private static final long serialVersionUID = -3966625274870774651L;

    @ApiModelProperty(value = "行数")
    private Long locationRow;

    @ApiModelProperty(value = "列数")
    private Long locationColumn;

    @ApiModelProperty(value = "芯片数")
    private Long chipNum;

    @ApiModelProperty(value = "转移规则")
    private String loadRule;

    @ApiModelProperty(value = "转移规则含义")
    private String loadRuleMeaning;
}
