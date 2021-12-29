package com.ruike.wms.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.modeling.domain.entity.MtSupplier;

/**
 * WmsSupplierVO
 *
 * @author liyuan.lv@hand-china.com 2020/05/06 16:55
 */
@Data
public class WmsSupplierVO implements Serializable {

    private static final long serialVersionUID = -6804387753620916329L;
    @ApiModelProperty(value = "供应商id")
    private String supplierId;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
}
