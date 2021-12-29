package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeNcDisposePlatformDTO35
 *
 * @author: chaonan.hu@hand-china.com 2020-12-23 13:46:23
 **/
@Data
public class HmeNcDisposePlatformDTO35 implements Serializable {
    private static final long serialVersionUID = 3521955601703254363L;

    private List<String> allMaterialList;

    private String routerStepId;
}
