package tarzan.order.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/1/7 3:04 下午
 */
public class MtEoBomDTO3 implements Serializable {
    private static final long serialVersionUID = 8009397418219585832L;
    @ApiModelProperty(value = "装配清单ID")
    private String bomComponentId;
    @ApiModelProperty(value = "步骤顺序")
    private Long sequence;
    @ApiModelProperty(value = "步骤识别码")
    private String step;
    @ApiModelProperty(value = "步骤描述")
    private String stepDesc;

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getStepDesc() {
        return stepDesc;
    }

    public void setStepDesc(String stepDesc) {
        this.stepDesc = stepDesc;
    }
}
