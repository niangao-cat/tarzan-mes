package tarzan.instruction.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtInstructionVO19
 * @description
 * @date 2019年12月18日 15:34
 */
public class MtInstructionVO19 implements Serializable {
    private static final long serialVersionUID = 4491840340161504994L;

    @ApiModelProperty(value = "指令ID")
    private String instructionId;
    @ApiModelProperty(value = "数量")
    private Double qty;
    @ApiModelProperty(value = "单位")
    private String uomId;

    public String getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(String instructionId) {
        this.instructionId = instructionId;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }
}
