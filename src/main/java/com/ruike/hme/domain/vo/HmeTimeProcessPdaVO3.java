package com.ruike.hme.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * HmeTimeProcessPdaVO3
 *
 * @author chaonan.hu@hand-china.com 2020-08-19 14:47:23
 **/
@Data
public class HmeTimeProcessPdaVO3 implements Serializable {
    private static final long serialVersionUID = -1727403258745244271L;

    @ApiModelProperty(value = "物料ID")
    private String materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "条码")
    private String barcode;

    @ApiModelProperty(value = "物料批ID")
    private String materialLotId;

    @ApiModelProperty(value = "主单位数量")
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "主单位ID")
    private String primaryUomId;

    @ApiModelProperty(value = "主单位")
    private String uomCode;

    @ApiModelProperty(value = "进站日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date siteInDate;

    @ApiModelProperty(value = "来源信息记录Id")
    private String sourceJobId;
}
