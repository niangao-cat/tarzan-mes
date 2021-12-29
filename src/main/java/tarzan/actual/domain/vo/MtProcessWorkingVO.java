package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;


/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 2020-02-10
 */
public class MtProcessWorkingVO implements Serializable {
    private static final long serialVersionUID = -2889692223257557703L;

    @ApiModelProperty("工序ID")
    private String operationId;

    @ApiModelProperty("步骤名称")
    private String stepName;
    
    @ApiModelProperty("工艺名称")
    private String operationName;

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
    
}

