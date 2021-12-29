package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * HmeNcDisposePlatformDTO32
 *
 * @author: chaonan.hu@hand-china.com 2020-11-24 10:29:34
 **/
@Data
public class HmeNcDisposePlatformDTO32 implements Serializable {
    private static final long serialVersionUID = -8172708880490045722L;

    private String materialId;

    private String siteId;

    private String materialLotId;

    private BigDecimal applyQty;

    private String uuid;

    private List<String> childNcCodeIdList;

    private Long userId;

    private String flag;

    private String eventId;

    private String eventRequestId;
}
