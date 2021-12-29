package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/12/2 14:28
 * @Author: ${yiyang.xie}
 */
public class MtEoVO41 implements Serializable {
    private static final long serialVersionUID = -4280822850801832779L;
    @ApiModelProperty("副合并来源执行作业列表")
    private String secondaryEoId;
    @ApiModelProperty("当前执行作业步骤实际")
    private String sourceEoStepActualId;

    public String getSecondaryEoId() {
        return secondaryEoId;
    }

    public void setSecondaryEoId(String secondaryEoId) {
        this.secondaryEoId = secondaryEoId;
    }

    public String getSourceEoStepActualId() {
        return sourceEoStepActualId;
    }

    public void setSourceEoStepActualId(String sourceEoStepActualId) {
        this.sourceEoStepActualId = sourceEoStepActualId;
    }
}
