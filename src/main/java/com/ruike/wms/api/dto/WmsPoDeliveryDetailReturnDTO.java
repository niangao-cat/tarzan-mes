package com.ruike.wms.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tarzan.instruction.domain.entity.MtInstruction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName WmsPoDeliveryDetailReturnDTO
 * @Deacription TODO
 * @Author ywz
 * @Date 2020/4/14 15:12
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class WmsPoDeliveryDetailReturnDTO extends MtInstruction  implements Serializable {
    private static final long serialVersionUID = -6453604578708485697L;
    public static final String FLAGY = "Y";

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料描述")
    private String materialName;

    @ApiModelProperty(value = "是否免检")
    private String qcFlag;

    @ApiModelProperty(value = "料废调换标识")
    private String exchangedFlag;

    @ApiModelProperty(value = "料废调數量")
    private String exchangeQty;

    @ApiModelProperty(value = "历史执行数量")
    private String actualQty;

    @ApiModelProperty(value = "特采标识")
    private String uaiFlag;

    @ApiModelProperty(value = "已料废调换数量")
    private String exchangedQty;

    @ApiModelProperty(value = "单位")
    private String uomCode;

    @ApiModelProperty(value = "行信息")
    private List<WmsPoDeliveryDetailLineReturnDTO> lineReturnDTOList;

    public String getQcFlag() {
        if (FLAGY.equals(qcFlag)) {
            return "免检";
        } else {
            return "非免检";
        }
    }

    @Getter
    @Setter
    @ToString
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class WmsPoDeliveryDetailLineReturnDTO  {

        @ApiModelProperty(value = "物料条码")
        private String materiallotCode;

        @ApiModelProperty(value = "物料编码")
        private String materialCode;

        @ApiModelProperty(value = "物料描述")
        private String materialName;

        @ApiModelProperty(value = "单位")
        private String uomCode;

        @ApiModelProperty("数量")
        private BigDecimal primaryUomQty;

        @ApiModelProperty(value = "质量状态")
        private String qualityStatus;

        @ApiModelProperty(value = "目标货位")
        private String locatorCode;

        @ApiModelProperty(value = "供应商批次")
        private String lot;

    }
}
