package com.ruike.wms.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 已收待上架看板VO
 * @Author tong.li
 * @Date 2020/4/24 12:54
 * @Version 1.0
 */
@Data
public class WmsCheckedWaitGroudingDTO implements Serializable {
    private static final long serialVersionUID = -4449783454475451438L;

    @ApiModelProperty(value = "租户id")
    private String tenantId;
    @ApiModelProperty(value = "单据ID")
    private String instructionDocId;
    @ApiModelProperty(value = "单据行ID")
    private String instructionId;
    @ApiModelProperty(value = "任务状态")
    private String taskStatus;
    @ApiModelProperty(value = "单据类型")
    private String instructionDocType;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "任务数量")
    private BigDecimal taskQty;
    @ApiModelProperty(value = "执行数量")
    private BigDecimal executeQty;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date creationDate;
    @ApiModelProperty(value = "最后更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date lastUpdateDate;
    @ApiModelProperty(value = "加急标识")
    private String urgentFlag;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "实际接收数量")
    private String actualReceiveQty;
    @ApiModelProperty(value = "特采标识")
    private String uaiFlag;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "单位")
    private String uomCode;
    @ApiModelProperty(value = "待上架天数")
    private BigDecimal waitStoragedDays;


}
