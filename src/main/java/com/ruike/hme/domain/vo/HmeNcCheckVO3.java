package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author sanfeng.zhang@hand-china.com 2020/11/25 21:26
 */
@Data
public class HmeNcCheckVO3 implements Serializable {

    private static final long serialVersionUID = 7587371022093239273L;

    private BigDecimal assembleQty;

    private BigDecimal scrappedQty;
}
