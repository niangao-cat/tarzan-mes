package com.ruike.wms.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tarzan.instruction.domain.entity.MtInstructionDoc;

import java.io.Serializable;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 送货单扫描返回DTO
 * @author: han.zhang
 * @create: 2020/04/07 14:36
 */
@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class WmsPoDeliveryScanReturnDTO extends MtInstructionDoc implements Serializable {
    private static final long serialVersionUID = 6165051373714851437L;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    @ApiModelProperty(value = "供应商描述")
    private String supplierName;

    @ApiModelProperty(value = "送货地址")
    private String customerSiteDescription;

    @ApiModelProperty(value = "加急标志")
    private String urgentFlag;

    @ApiModelProperty(value = "行信息")
    private List<WmsPoDeliveryScanLineReturnDTO> lineReturnDTOList;

    @ApiModelProperty(value = "接收批次号")
    private String number;

}