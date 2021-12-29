package com.ruike.qms.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * QmsPqcHeaderVO12
 * @description: 巡检平台海马汇版产线LOV返回对象
 * @author: chaonan.hu@hand-china.com 2020-10-23 10:28
 **/
@Data
public class QmsPqcHeaderVO12 implements Serializable {
    private static final long serialVersionUID = -734033324999629928L;

    private String prodLineId;

    private String prodLineCode;

    private String prodLineName;

    private Boolean hide = false;

    private String queryInfo;
}
