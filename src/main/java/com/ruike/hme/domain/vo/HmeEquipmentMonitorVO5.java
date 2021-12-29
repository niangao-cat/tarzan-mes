package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeEquipmentMonitorVO5
 *
 * @author chaonan.hu@hand-china.com 2020/07/17 10:10:12
 */
@Data
public class HmeEquipmentMonitorVO5 implements Serializable {
    private static final long serialVersionUID = 3729664030975727726L;

    private Long runEquipmentNumber;

    private Long errorEquipmentNumber;

    private String averagePercentage;
}
