package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmeProductionPrintVO6
 *
 * @author chaonan.hu@hand-china.com 2021/10/18 10:14
 */
@Data
public class HmeProductionPrintVO7 implements Serializable {
    private static final long serialVersionUID = -517621026142362629L;

    private String eoId;

    private String workcellId;

    private String jobId;

    private String materialLotId;

    private String materialId;

    private BigDecimal qty;

    private String materialLotCode;

    private String identification;

    private String attribute5;

    private String reworkMaterialLot;
}
