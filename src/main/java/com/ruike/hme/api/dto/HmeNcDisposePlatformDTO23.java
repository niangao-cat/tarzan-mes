package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: chaonan.hu@hand-china.com 2020-07-14 19:52:34
 **/
@Data
public class HmeNcDisposePlatformDTO23 implements Serializable {
    private static final long serialVersionUID = 5145149674996463878L;

    private Long number;

    private String materialId;

    private String materialCode;

    private String materialName;

    private String ncComponentTempId;

    private List<HmeNcDisposePlatformDTO24> materialLotList;
}
