package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeCenterKanbanLineVO
 *
 * @author: chaonan.hu@hand-china.com 2022/05/28 16:40:12
 **/
@Data
public class HmeCenterKanbanLineVO implements Serializable {
    private static final long serialVersionUID = 5419972495257793710L;

    @ApiModelProperty(value = "头Id")
    private String centerKanbanHeaderId;

    @ApiModelProperty(value = "行主键")
    private String centerKanbanLineId;

    @ApiModelProperty(value = "工序ID")
    private String workcellId;

    @ApiModelProperty(value = "工序编码")
    private String workcellCode;

    @ApiModelProperty(value = "工序名称")
    private String workcellName;

    @ApiModelProperty(value = "直通率")
    private BigDecimal throughRate;

    @ApiModelProperty(value = "有效性")
    @LovValue(value = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;

    @ApiModelProperty(value = "有效性")
    private String enableFlagMeaning;
}
