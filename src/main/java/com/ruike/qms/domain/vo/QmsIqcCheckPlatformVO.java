package com.ruike.qms.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * QmsIqcCheckPlatformVO
 * @description:
 * @author: chaonan.hu@hand-china.com 2020/9/26 13:45
 **/
@Data
public class QmsIqcCheckPlatformVO implements Serializable {
    private static final long serialVersionUID = -1373283010972744513L;

    private String actualDetailId;

    private String materialLotId;

    private String materialLotCode;

    private BigDecimal ncQty;
}
