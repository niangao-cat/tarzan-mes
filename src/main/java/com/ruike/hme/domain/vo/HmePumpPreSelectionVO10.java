package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmePumpPreSelectionVO10
 *
 * @author: chaonan.hu@hand-china.com 2021/09/01 20:37:13
 **/
@Data
public class HmePumpPreSelectionVO10 implements Serializable {
    private static final long serialVersionUID = 6887207473255731245L;

    private String materialLotId;

    private String ruleLineId;
}
