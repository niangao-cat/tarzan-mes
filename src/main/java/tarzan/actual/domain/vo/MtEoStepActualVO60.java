package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class MtEoStepActualVO60 implements Serializable {

    private static final long serialVersionUID = -9138712609407580506L;
    @ApiModelProperty(value = "执行作业实绩ID")
    private String eoStepActualId;

    @ApiModelProperty(value = "验证结果")
    private String validateResult;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getValidateResult() {
        return validateResult;
    }

    public void setValidateResult(String validateResult) {
        this.validateResult = validateResult;
    }

    public MtEoStepActualVO60() {
    }

    public MtEoStepActualVO60(String eoStepActualId, String validateResult) {
        this.eoStepActualId = eoStepActualId;
        this.validateResult = validateResult;
    }
}


