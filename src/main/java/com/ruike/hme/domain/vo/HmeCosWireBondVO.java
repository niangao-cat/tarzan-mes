package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class HmeCosWireBondVO implements Serializable {

    private static final long serialVersionUID = -2364573081941779469L;

    private String workOrderNum;

    private String workOrderId;

    private String materialId;

    private String materialCode;

    private String materialLotCode;

    private String materialLotId;

    private Double primaryUomQty;

    private String cosType;

    private String lotNo;

    private String avgWaveLength;

    private String containerTypeDescription;

    private String wafer;

    private String jobId;
}
