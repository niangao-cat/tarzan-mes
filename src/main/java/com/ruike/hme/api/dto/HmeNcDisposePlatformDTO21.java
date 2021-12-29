package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: chaonan.hu@hand-china.com 2020-07-06 10:24:34
 **/
@Data
public class HmeNcDisposePlatformDTO21 implements Serializable {
    private static final long serialVersionUID = -171426411023432055L;

    private String snNumber;

    private String materialId;

    private String materialLotId;

    private String materialLotCode;

    private BigDecimal scrapQty;
}
