package tarzan.modeling.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModWorkcellManufacturingDTO implements Serializable {
    private static final long serialVersionUID = 6078587107075271203L;

    @ApiModelProperty(value = "工作单元ID，标识唯一工作单元", required = true)
    private String workcellId;
    @ApiModelProperty(value = "可向前操作的班次数")
    private Long forwardShiftNumber;
    @ApiModelProperty(value = "可向后操作的班次数")
    private Long backwardShiftNumber;

    /**
     * @return 工作单元ID，标识唯一工作单元
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 可向前操作的班次数
     */
    public Long getForwardShiftNumber() {
        return forwardShiftNumber;
    }

    public void setForwardShiftNumber(Long forwardShiftNumber) {
        this.forwardShiftNumber = forwardShiftNumber;
    }

    /**
     * @return 可向后操作的班次数
     */
    public Long getBackwardShiftNumber() {
        return backwardShiftNumber;
    }

    public void setBackwardShiftNumber(Long backwardShiftNumber) {
        this.backwardShiftNumber = backwardShiftNumber;
    }
}
