package com.ruike.itf.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ItfMonthlyPlanIfaceDTO
 *
 * @author: chaonan.hu@hand-china.com 2021-06-01 14:21:59
 **/
@Data
public class ItfMonthlyPlanIfaceDTO implements Serializable {
    private static final long serialVersionUID = -8124325033623051394L;

    private String MATERIAL;

    private String PLANT;

    private String PROD_SUPER;

    private String MONTH;

    private BigDecimal QUANTITY;
}
