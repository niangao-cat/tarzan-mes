package com.ruike.qms.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: 外协发货单展示数据DTO
 * @auther chaonan.hu
 * @date 2020/6/10
 **/
@Getter
@Setter
@ToString
public class QmsInvoiceDataReturnDTO implements Serializable {
    private static final long serialVersionUID = 5086782957421973144L;

    @ApiModelProperty(value = "外协发货单头信息")
    private QmsInvoiceHeadReturnDTO qmsInvoiceHeadReturnDTO;

    @ApiModelProperty(value = "外协发货单组件信息")
    private List<QmsInvoiceLineReturnDTO> qmsInvoiceLineReturnDTOList;

    @ApiModelProperty(value = "选中的采购订单行信息")
    private List<QmsInvoiceInstructionDTO> mtInstructionS;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date earilyDemandTime;
}
