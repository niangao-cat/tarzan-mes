package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * HmeEoJobPumpCombVO6
 *
 * @author: chaonan.hu@hand-china.com 2021/09/07 16:28
 **/
@Data
public class HmeEoJobPumpCombVO6 implements Serializable {
    private static final long serialVersionUID = 4742170821169822584L;

    private String pumpSelectionDetailsId;

    private String pumpPreSelectionId;

    private Long selectionOrder;
}
