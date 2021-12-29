package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 送货单号生成及合并行的返回DTO
 * @author: han.zhang
 * @create: 2020/04/01 11:19
 */
@Getter
@Setter
@ToString
public class WmsDliveryNumReturnDTO {
    /**
     * 送货单号
     */
    private String number;
    /**
     * 合并后的行
     */
    private List<DeliveryNumReturnLineDTO> lineList;

    @Getter
    @Setter
    @ToString
    public static class  DeliveryNumReturnLineDTO{
        @ApiModelProperty(value = "行号")
        private String instructionLineNum;
        @ApiModelProperty(value = "单据行号")
        private String instructionNum;
        @ApiModelProperty(value = "物料id")
        private String materialId;
        @ApiModelProperty(value = "物料code")
        private String materialCode;
        @ApiModelProperty(value = "物料版本")
        private String materialVersion;
        @ApiModelProperty(value = "制单数量")
        private BigDecimal quantity2;
        @ApiModelProperty(value = "单位")
        private String primaryUomCode;
        @ApiModelProperty(value = "物料描述")
        private String materialName;
        @ApiModelProperty(value = "单位")
        private String uomId;
        @ApiModelProperty(value = "返回的分组中的头id和行ID汇总")
        private List<WmsPoDeliveryDTO.DeliveryOrderLineDTO.OrderIdDTO> idDtoS;
        @ApiModelProperty(value = "销售订单")
        private String soNum;
        @ApiModelProperty(value = "销售订单行")
        private String soLineNum;
        @ApiModelProperty(value = "样品标识")
        private String sampleFlag;
        @ApiModelProperty(value = "仓库id")
        private String toLocatorId;
    }
}