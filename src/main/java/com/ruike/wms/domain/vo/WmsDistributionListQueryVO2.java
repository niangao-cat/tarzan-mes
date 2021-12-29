package com.ruike.wms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class WmsDistributionListQueryVO2 implements Serializable {
    private static final long serialVersionUID = 5922378474574703266L;
    @ApiModelProperty("配送单号")
    private String instructionDocNum;

    @ApiModelProperty("配送单行号")
    private String instructionNum;

    @ApiModelProperty("物料批")
    private String materialLot;

    @ApiModelProperty("物料批状态")
    private String materialStatus;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料版本")
    private String materialVersion;

    @ApiModelProperty("物料描述")
    private String materialName;

    @ApiModelProperty("实际执行数量")
    private BigDecimal primaryUomQty;

    @ApiModelProperty("批次")
    private String lot;

    @ApiModelProperty("单位")
    private String uomCode;

    @ApiModelProperty("批次ID")
    private String materialLotId;

    @ApiModelProperty("物流器具")
    private String containerNum;

    @ApiModelProperty("备料时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;

    @ApiModelProperty("备料人员")
    private String createdBy;

    @ApiModelProperty("现处货位")
    private String locatorCode;

    @ApiModelProperty("明细ID")
    private String instructionDetailId;
}
