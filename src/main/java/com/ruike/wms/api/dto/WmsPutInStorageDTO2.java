package com.ruike.wms.api.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * WmsPutInStorageDTO
 *
 * @author liyuan.lv@hand-china.com 2020/04/03 18:25
 */

@Data
public class WmsPutInStorageDTO2 implements Serializable {

    private static final long serialVersionUID = 8064820634484066729L;
    private String siteId;
    private String locatorCode;
}
