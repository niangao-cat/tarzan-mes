package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtRouterVO21 extends MtRouterVO8 implements Serializable {

    private static final long serialVersionUID = 6322246763068708782L;
    @ApiModelProperty(value = "验证结果")
    private String validateResult;

    public String getValidateResult() {
        return validateResult;
    }

    public void setValidateResult(String validateResult) {
        this.validateResult = validateResult;
    }
}
