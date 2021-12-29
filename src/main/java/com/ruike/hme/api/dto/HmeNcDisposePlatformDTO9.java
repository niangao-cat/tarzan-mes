package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: chaonan.hu@hand-china.com 2020-06-30 19:03:29
 **/
@Data
public class HmeNcDisposePlatformDTO9 implements Serializable {
    private static final long serialVersionUID = -6207270385012215149L;

    private List<HmeNcDisposePlatformDTO8> hmeNcDisposePlatformDTO8List;

    private Long processNcTypeSize;
}
