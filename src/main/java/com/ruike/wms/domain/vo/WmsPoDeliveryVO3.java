package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 采购订单查询返回的VO
 * @author: han.zhang
 * @create: 2020/03/19 11:06
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WmsPoDeliveryVO3 {

    private static final long serialVersionUID = 956965049615219041L;
    @ApiModelProperty(value = "送货单ID")
    private String instructionDocId;
    @ApiModelProperty(value = "送货单号")
    private String instructionDocNum;
    @ApiModelProperty(value = "送货单状态")
    private String instructionDocStatus;
    @ApiModelProperty(value = "供应商")
    private String supplierId;
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;
    @ApiModelProperty(value = "预计送达日期")
    private LocalDate demandTime;
    @ApiModelProperty(value = "备注")
    private String remark;

    private List<WmsPoDeliveryVO4> wmsPoDeliveryVo4List;
}