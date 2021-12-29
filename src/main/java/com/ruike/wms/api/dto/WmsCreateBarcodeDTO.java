package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 条码创建DTO
 * @author: han.zhang
 * @create: 2020/04/08 10:58
 */
@Getter
@Setter
@ToString
public class WmsCreateBarcodeDTO implements Serializable {
    private static final long serialVersionUID = 361874439614836999L;

    @ApiModelProperty(value = "工厂")
    @NotNull
    private String siteId;

    @ApiModelProperty(value = "状态")
    @NotNull
    private String status;

    @ApiModelProperty(value = "质量状态")
    @NotNull
    private String qualityStatus;

    @ApiModelProperty(value = "物料")
    @NotNull
    private String materialId;

    @ApiModelProperty(value = "单位")
    @NotNull
    private String primaryUomId;

    @ApiModelProperty(value = "条码数量")
    @NotNull
    private BigDecimal primaryUomQty;

    @ApiModelProperty(value = "创建数量")
    @NotNull
    private Long createQty;

    @ApiModelProperty(value = "送货单号")
    @NotNull
    private String deliveryNum;

    @ApiModelProperty(value = "是否有效")
    @NotNull
    private String enableflag;

    @ApiModelProperty(value = "供应商")
    @NotNull
    private String supplierId;

    @ApiModelProperty(value = "创建原因")
    @NotNull
    private String createReason;

    @ApiModelProperty(value = "送货单头ID")
    @NotNull
    private String instructionDocId;

    @ApiModelProperty(value = "送货单行")
    private String instructionId;

    @ApiModelProperty(value = "物料版本")
    private String materialVersion;

    @ApiModelProperty(value = "创建条码号需要的参数")
    @NotNull
    private NumrangeGenerateDTO numrangeGenerateDTOS;

    @Getter
    @Setter
    @ToString
    public static class NumrangeGenerateDTO{
        private String objectCode;
        private String objectTypeCode;
        private String siteId;
        private CallObjectCodeDTO callObjectCodeList;

        @Getter
        @Setter
        @ToString
        public static class CallObjectCodeDTO{
            private String siteCode;
        }
    }

}