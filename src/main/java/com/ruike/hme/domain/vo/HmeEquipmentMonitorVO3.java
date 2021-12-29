package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeEquipmentMonitorVO3
 *
 * @author chaonan.hu@hand-china.com 2020/07/16 19:27:09
 */
@Data
public class HmeEquipmentMonitorVO3 implements Serializable {
    private static final long serialVersionUID = -31256549550211566L;

    private String prodLineId;

    private String prodLineName;
}
