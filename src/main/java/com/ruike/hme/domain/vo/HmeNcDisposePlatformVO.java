package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chaonan.hu@hand-china.com 2020-07-10 14:33:48
 **/
@Data
public class HmeNcDisposePlatformVO implements Serializable {
    private static final long serialVersionUID = 672668277618563194L;

    private String materialCode;

    private String materialName;
}
