package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: tarzan-mes->HmeEoWorkingVO2
 * @description:
 * @author: chaonan.hu@hand-china.com 2021/01/13 11:27:34
 **/
@Data
public class HmeEoWorkingVO2 implements Serializable {
    private static final long serialVersionUID = -3275701269819840045L;

    private String materialId;

    private String materialCode;

    private String materialName;

    private String workcellId;

    private String workcellName;

    private Long sequence;

    private Long sequenceTwo;

    private Double workingQtySum;

    private Double completedQtySum;

    private Double queueQtySum;
}
