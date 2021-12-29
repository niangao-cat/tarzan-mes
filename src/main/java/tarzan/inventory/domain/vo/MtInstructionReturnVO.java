package tarzan.inventory.domain.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;
import tarzan.instruction.domain.entity.MtInstruction;

/**
 * @program: tarzan-mes
 * @description: 采购订单行表返回dto
 * @author: han.zhang
 * @create: 2020/03/19 18:20
 */
@Getter
@Setter
@ToString
public class MtInstructionReturnVO extends MtInstruction {
    private static final long serialVersionUID = -2120921846535275004L;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    @ApiModelProperty(value = "单位")
    private String primaryUomCode;
    @ApiModelProperty(value = "仓库")
    private String locatorName;
    @ApiModelProperty(value = "已接收数量")
    private String receivedQty;
    @ApiModelProperty(value = "已制单数量")
    private String quantityOrdered;
    @ApiModelProperty(value = "行号")
    private String instructionLineNum;
    @ApiModelProperty(value = "物料版本")
    private String materialVersion;
    @ApiModelProperty(value = "送货单制单数量之和")
    private Double poQuantitySum;
    @ApiModelProperty(value = "销售订单")
    private String soNum;
    @ApiModelProperty(value = "销售订单行")
    private String soLineNum;
    @ApiModelProperty(value = "可制单数量")
    private BigDecimal availableOrderQuantity;
    @ApiModelProperty(value = "订单类型")
    @LovValue(lovCode = "WMS.PO_LINE.TYPE",meaningField="poLineTypeMeaning")
    private String poLineType;
    @ApiModelProperty(value = "订单类型含义")
    private String poLineTypeMeaning;
    @ApiModelProperty(value = "样品标识")
    private String sampleFlag;
}