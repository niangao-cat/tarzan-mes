package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmeCosPatchPdaVO3
 *
 * @author: chaonan.hu@hand-china.com 2020/8/27 10:42:27
 **/
@Data
public class HmeCosPatchPdaVO3 implements Serializable {
    private static final long serialVersionUID = 4504032210490561042L;

    private List<HmeCosPatchPdaVO2> materialLotCodeList;

    private BigDecimal completedQty;

    private Long maxNumber;

    @ApiModelProperty(value = "金锡比")
    private String auSnRatio;
}
