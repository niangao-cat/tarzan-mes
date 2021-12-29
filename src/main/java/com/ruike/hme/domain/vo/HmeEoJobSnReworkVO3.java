package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HmeEoJobSnReworkVO3 implements Serializable {
    private static final long serialVersionUID = 697505306890345763L;
    @ApiModelProperty(value = "是否投料")
    private Integer isReleased;
    @ApiModelProperty(value = "截止时间(倒计时)")
    private String deadLineDate;
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty(value = "物料批编码")
    private String materialLotCode;
    @ApiModelProperty(value = "该物料ID")
    private String materialId;
    @ApiModelProperty(value = "实物在主计量单位下的数量")
    private BigDecimal primaryUomQty;
    @ApiModelProperty(value = "货位ID")
    private String locatorId;
    @ApiModelProperty(value = "货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "批次")
    private String lot;
    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;
    @ApiModelProperty(value = "创建时间")
    private Date creationDate;
    @ApiModelProperty(value = "条码有效性")
    private String enableFlag;
    @ApiModelProperty(value = "质量状态")
    private String qualityStatus;
    @ApiModelProperty(value = "条码冻结标识")
    private String freezeFlag;
    @ApiModelProperty(value = "盘点停用标识")
    private String stocktakeFlag;
    @ApiModelProperty(value = "主键ID")
    private String jobMaterialId;
}
