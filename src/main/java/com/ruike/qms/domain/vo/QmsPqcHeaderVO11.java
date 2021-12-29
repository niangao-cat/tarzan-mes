package com.ruike.qms.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * QmsPqcHeaderVO11
 * @description: 巡检平台海马汇版车间LOV返回对象
 * @author: chaonan.hu@hand-china.com 2020-10-23 10:15
 **/
@Data
public class QmsPqcHeaderVO11 implements Serializable {
    private static final long serialVersionUID = 9009802412609429744L;

    private String areaId;

    private String areaCode;

    private String description;

    private String parentOrganizationId;

    private Boolean hide = false;

    private String queryInfo;
}
