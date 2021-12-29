package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * HmeCosYieldComputeVO3
 *
 * @author: chaonan.hu@hand-china.com 2021/09/17 16:14
 **/
@Data
public class HmeCosYieldComputeVO3 implements Serializable {
    private static final long serialVersionUID = 4174874136633290818L;

    private String materialLotId;

    private BigDecimal attribute6;
}
