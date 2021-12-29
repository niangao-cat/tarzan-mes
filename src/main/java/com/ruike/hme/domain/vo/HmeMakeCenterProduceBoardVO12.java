package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author sanfeng.zhang@hand-china.com 2021/6/2 1:11
 */
@Data
public class HmeMakeCenterProduceBoardVO12 implements Serializable {

    private static final long serialVersionUID = 3086669382993937804L;

    @ApiModelProperty("看板头主键")
    private String centerKanbanHeaderId;
    @ApiModelProperty("工序id")
    private String processId;
    @ApiModelProperty("工序")
    private String processName;
    @ApiModelProperty("工序对应直通率")
    private BigDecimal processThroughRate;
    @ApiModelProperty("工序目标直通率")
    private BigDecimal targetThroughRate;
    @ApiModelProperty("直通率")
    private BigDecimal throughRate;
    @ApiModelProperty("测试工序标识")
    @JsonIgnore
    private String cosTestFlag;
}
