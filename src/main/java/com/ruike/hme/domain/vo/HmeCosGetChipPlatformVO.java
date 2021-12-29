package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeCosGetChipPlatformVO
 *
 * @author: chaonan.hu@hand-china.com 2020/12/01 11:07
 **/
@Data
public class HmeCosGetChipPlatformVO implements Serializable {
    private static final long serialVersionUID = -7873684062065978196L;

    private String routerStepId;

    private String operationId;
}
