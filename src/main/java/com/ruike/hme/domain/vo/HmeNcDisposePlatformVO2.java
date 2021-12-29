package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeNcDisposePlatformVO2
 *
 * @author: chaonan.hu@hand-china.com 2020-09-10 14:38:23
 **/
@Data
public class HmeNcDisposePlatformVO2 implements Serializable {
    private static final long serialVersionUID = 8080170588989223348L;

    private BigDecimal scrapQty;
}
