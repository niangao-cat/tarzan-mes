package com.ruike.wms.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * WmsWarehouseLocatorVO
 *
 * @author liyuan.lv@hand-china.com 2020/04/30 18:25
 */
@Data
public class WmsWarehouseLocatorVO implements Serializable {
    private String locatorId;
    private String locatorCode;
    private String locatorName;
}
