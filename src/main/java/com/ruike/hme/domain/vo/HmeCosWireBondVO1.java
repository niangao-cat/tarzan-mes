package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HmeCosWireBondVO1 implements Serializable {
    private static final long serialVersionUID = -414497606612301551L;

    private Long surplusCosNum;

    private Long cosNum;

    private String workOrderId;

    private List<HmeCosWireBondVO> hmeCosWireBondVOList;
}
