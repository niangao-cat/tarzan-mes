package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeNcDisposePlatformVO6
 *
 * @author: chaonan.hu@hand-china.com 2020-11-24 14:29:34
 **/
@Data
public class HmeNcDisposePlatformVO6 implements Serializable {
    private static final long serialVersionUID = 6409966329762607026L;

    private String bomComponentId;

    private String workOrderId;

    private String assembleExcessFlag;

    private String eventId;

    private String materialId;

    private String materialLotId;

    private BigDecimal applyQty;

    private BigDecimal assembleQtySum;

    private BigDecimal lineAttribute5;

}
