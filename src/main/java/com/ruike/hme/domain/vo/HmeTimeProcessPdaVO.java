package com.ruike.hme.domain.vo;

import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * HmeTimeProcessPdaVO
 *
 * @author chaonan.hu@hand-china.com 2020-08-19 13:45:23
 **/
@Data
public class HmeTimeProcessPdaVO implements Serializable {
    private static final long serialVersionUID = -5915847024649258622L;

    private String equipmentId;

    private String equipmentCode;

    private String equipmentName;

    @LovValue(value = "HME.STATION_EQ_STATUS", meaningField = "equipmentStatusMeaning")
    private String equipmentStatus;

    private String equipmentStatusMeaning;

    private String workcellId;

    private String workcellName;
}
