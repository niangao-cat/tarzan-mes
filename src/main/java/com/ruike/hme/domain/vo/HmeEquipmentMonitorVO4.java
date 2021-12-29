package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeEquipmentMonitorVO4
 *
 * @author chaonan.hu@hand-china.com 2020/07/16 19:27:09
 */
@Data
public class HmeEquipmentMonitorVO4 implements Serializable {
    private static final long serialVersionUID = -4969296182505953656L;

    private String equipmentId;

    private String equipmentCode;

    private String equipmentName;

    private String workcellId;

    private String workcellName;

    private String percentage;
}
