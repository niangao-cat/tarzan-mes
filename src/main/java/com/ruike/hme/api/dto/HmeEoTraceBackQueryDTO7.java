package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 产品追溯
 *
 * @author chaonan.hu@hand-china.com 2020-07-15 09:42:13
 */
@Data
public class HmeEoTraceBackQueryDTO7 implements Serializable {
    private static final long serialVersionUID = -9143327358548208258L;

    private String jobMaterialId;

    private String materialCode;

    private String materialName;

    private String materialLotCode;

    private String releaseQtyStr;

    private Integer releaseQty;

    private Boolean children;

    private String itemGroupDescription;

}
