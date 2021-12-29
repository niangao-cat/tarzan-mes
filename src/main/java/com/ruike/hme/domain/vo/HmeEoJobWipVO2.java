package com.ruike.hme.domain.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * HmeEoJobWipVO2
 *
 * @author liyuan.lv@hand-china.com 2020/04/21 14:44
 */
@Data
public class HmeEoJobWipVO2 implements Serializable {

    private static final long serialVersionUID = 5319468127493804778L;

    private String workcellId;
    private String eoId;
    private String eoStepActualId;
    private String routerStepId;
    private String operationId;
    private String materialId;
    private String materialCode;
    private String materialName;
    private String uomName;
    private Double workingQty;
    private Double completedQty;
    private Double queueQty;
    private String status;
}
