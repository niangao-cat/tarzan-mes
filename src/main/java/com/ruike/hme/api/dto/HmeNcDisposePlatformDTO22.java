package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: chaonan.hu@hand-china.com 2020-07-06 10:24:34
 **/
@Data
public class HmeNcDisposePlatformDTO22 implements Serializable {
    private static final long serialVersionUID = -82867030189434596L;

    private String ncComponentTempId;

    private String materialLotId;

    private String materialLotCode;
}
