package com.ruike.wms.domain.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * WmsMaterialSiteVO
 *
 * @author liyuan.lv@hand-china.com 2020/05/06 15:53
 */
@Data
public class WmsMaterialSiteVO implements Serializable {

    private static final long serialVersionUID = -2271840591614095929L;
    private String siteId;
    private String materialId;
    private String materialCode;
    private String materialName;
}
