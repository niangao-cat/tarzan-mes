package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeEoJobPumpCombVO5
 *
 * @author: chaonan.hu@hand-china.com 2021/09/07 10:15
 **/
@Data
public class HmeEoJobPumpCombVO5 implements Serializable {
    private static final long serialVersionUID = -9026332395854715585L;

    private String parameterCode;

    private BigDecimal minValue;

    private BigDecimal maxValue;

    private String tagId;

    private BigDecimal result;
}
