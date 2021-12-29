package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用于eoStepCompletedBatchValidate返回参数
 * 
 * @author benjamin
 * @date 2020/10/27 9:30 AM
 */
public class MtEoStepActualVO54 implements Serializable {
    private static final long serialVersionUID = 8831636683710589255L;

    @ApiModelProperty("执行作业步骤实绩Id")
    private String eoStepActualId;
    @ApiModelProperty("验证结果")
    private String result;

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
