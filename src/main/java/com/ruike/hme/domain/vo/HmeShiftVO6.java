package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeShiftVO6
 *
 * @author chaonan.hu@hand-china.com 2020/07/29 11:31:17
 */
@Data
public class HmeShiftVO6 implements Serializable {
    private static final long serialVersionUID = 2719780161496614668L;

    private String positionId;

    private String supervisorFlag;
}
