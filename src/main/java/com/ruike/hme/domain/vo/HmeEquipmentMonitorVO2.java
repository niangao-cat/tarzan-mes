package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeEquipmentMonitorVO2
 *
 * @author chaonan.hu@hand-china.com 2020/07/16 19:27:09
 */
@Data
public class HmeEquipmentMonitorVO2 implements Serializable {
    private static final long serialVersionUID = -1269463505086693955L;

    private String workshopId;

    private String workshopName;
}
