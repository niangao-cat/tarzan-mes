package com.ruike.wms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 生成送货单号DTO
 * @author: han.zhang
 * @create: 2020/03/31 15:46
 */
@Getter
@Setter
@ToString
public class WmsDeliveryNumberCreateDTO implements Serializable {
    private static final long serialVersionUID = -3503798556614597480L;
    @ApiModelProperty(value = "")
    private String objectCode;
    @ApiModelProperty(value = "")
    private String objectTypeCode;
    @ApiModelProperty(value = "")
    private NumberCreateInner callObjectCodeList;

    @ApiModelProperty(value = "")
    private String siteId;

    @ApiModelProperty(value = "缓存中的采购订单行")
    private List<InstructionLineList> instructionLineList;

    @Getter
    @Setter
    @ToString
    public static class  NumberCreateInner{
        @ApiModelProperty(value = "工厂编码")
        private String siteCode;
        @ApiModelProperty(value = "供应商编码")
        private String supplierCode;
    }

    @Getter
    @Setter
    public static class InstructionLineList{
        @ApiModelProperty(value = "采购订单头id")
        private String poId;
        @ApiModelProperty(value = "单据编号")
        private String instructionDocNum;
        @ApiModelProperty(value = "采购订单行id")
        private String poLineId;
        @ApiModelProperty(value = "单据行号")
        private String instructionNum;
        @ApiModelProperty(value = "物料id")
        private String materialId;
        @ApiModelProperty(value = "物料code")
        private String materialCode;
        @ApiModelProperty(value = "物料描述")
        private String materialName;
        @ApiModelProperty(value = "物料版本")
        private String materialVersion;
        @ApiModelProperty(value = "单位id")
        private String uomId;
        @ApiModelProperty(value = "制单数量")
        private BigDecimal quantity;
        @ApiModelProperty(value = "单位")
        private String primaryUomCode;
        @ApiModelProperty(value = "销售订单")
        private String soNum;
        @ApiModelProperty(value = "销售订单行")
        private String soLineNum;
        @ApiModelProperty(value = "仓库id")
        private String toLocatorId;
    }
}