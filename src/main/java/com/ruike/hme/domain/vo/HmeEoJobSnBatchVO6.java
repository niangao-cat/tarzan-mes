package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: penglin.sui
 * @Date: 2020/11/17 21:35
 * @Description: 组件条码
 */

@Data
public class HmeEoJobSnBatchVO6 implements Serializable {
    private static final long serialVersionUID = 7335877213910637507L;
    @ApiModelProperty(value = "主键ID")
    private String jobMaterialId;
    @ApiModelProperty(value = "物料类型")
    private String materialType;
    @ApiModelProperty(value = "工序作业ID")
    private String jobId;
    @ApiModelProperty(value = "工位ID")
    private String workCellId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "序号")
    private String lineNumber;
    @ApiModelProperty(value = "是否投料")
    private Integer isReleased;
    @ApiModelProperty(value = "条码ID")
    private String materialLotId;
    @ApiModelProperty(value = "条码编码")
    private String materialLotCode;
    @ApiModelProperty(value = "单位用量")
    private BigDecimal primaryUomQty;
    @ApiModelProperty(value = "截止时间(倒计时)")
    private String deadLineDate;
    @ApiModelProperty(value = "删除标识")
    private String deleteFlag;
    @ApiModelProperty(value = "库位ID")
    private String locatorId;
    @ApiModelProperty(value = "库位编码")
    private String locatorCode;
    @ApiModelProperty(value = "批次")
    private String lot;
    @ApiModelProperty(value = "供应商批次")
    private String supplierLot;
    @ApiModelProperty(value = "站点ID")
    private String siteId;
    @ApiModelProperty(value = "创建时间")
    private Date creationDate;
    @ApiModelProperty(value = "条码有效性")
    private String enableFlag;
    @ApiModelProperty(value = "物料批冻结标识")
    private String freezeFlag;
    @ApiModelProperty(value = "盘点停用标识")
    private String stocktakeFlag;
    @ApiModelProperty(value = "条码剩余数量")
    private BigDecimal remainQty;
    @ApiModelProperty(value = "条码投料量")
    private BigDecimal releaseQty;
    @ApiModelProperty(value = "上层虚拟件物料编码")
    private String topVirtualMaterialCode;

    @ApiModelProperty(value = "电压")
    private BigDecimal voltageValue;
    @ApiModelProperty(value = "功率")
    private BigDecimal powerValue;
}
