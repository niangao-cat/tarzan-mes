package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * HmePrepareMaterialVO
 *
 * @author liyuan.lv@hand-china.com 2020/06/29 17:35
 */
@Data
public class HmePrepareMaterialVO implements Serializable {

    private static final long serialVersionUID = -2208459498863385008L;
    private String materialId;
    private String materialCode;
    private String materialName;
    private BigDecimal preparedQty;
    private BigDecimal componentQty;
    private String availableTime;
    private String materialType;
    private String materialPrepareType;
    private String bomComponentId;
    private Long lineNumber;
    private String bomId;
}
