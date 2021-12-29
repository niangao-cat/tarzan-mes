package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/23 18:00
 * @Description:
 */
public class MtEoStepActualVO41 implements Serializable {
    private static final long serialVersionUID = 5622704392220988794L;

    @ApiModelProperty("步骤实绩ID")
    private String eoStepActualId;
    @ApiModelProperty("松散标识")
    private String relaxedFlowFlag;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getRelaxedFlowFlag() {
        return relaxedFlowFlag;
    }

    public void setRelaxedFlowFlag(String relaxedFlowFlag) {
        this.relaxedFlowFlag = relaxedFlowFlag;
    }
}
