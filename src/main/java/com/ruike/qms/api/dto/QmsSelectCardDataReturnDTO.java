package com.ruike.qms.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruike.qms.domain.entity.QmsIqcHeader;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 查询卡片属性
 * @author: han.zhang
 * @create: 2020/04/29 14:56
 */
@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class QmsSelectCardDataReturnDTO extends QmsIqcHeader implements Serializable {

    private static final long serialVersionUID = 7479665814062094918L;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "单位编码")
    private String uomCode;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "货位编码")
    private String locatorCode;

    @ApiModelProperty(value = "货位名称")
    private String locatorName;

    @ApiModelProperty(value = "未检天数")
    private String testDay;

    @ApiModelProperty(value = "送货单")
    private String instructionDocNum;
}