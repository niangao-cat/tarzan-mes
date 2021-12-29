package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: chaonan.hu@hand-china.com 2020-07-06 10:17:46
 **/
@Data
public class HmeNcDisposePlatformDTO20 implements Serializable {
    private static final long serialVersionUID = 1463745533839810588L;

    private String workcellId;

    private List<HmeNcDisposePlatformDTO21> hmeNcDisposePlatformDTO21List;
}
