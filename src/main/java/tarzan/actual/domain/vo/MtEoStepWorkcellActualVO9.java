package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtEoStepWorkcellActualVO9
 * @description
 * @date 2019年12月04日 13:57
 */
public class MtEoStepWorkcellActualVO9 implements Serializable {

    private static final long serialVersionUID = 640523963279168405L;

    @ApiModelProperty(value = "执行作业")
    private List<MtEoStepWorkcellActualVO11> eoIdAndTime;
    @ApiModelProperty(value = "工艺")
    private String operationId;
    @ApiModelProperty(value = "工作单元")
    private String workcellId;

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public List<MtEoStepWorkcellActualVO11> getEoIdAndTime() {
        return eoIdAndTime;
    }

    public void setEoIdAndTime(List<MtEoStepWorkcellActualVO11> eoIdAndTime) {
        this.eoIdAndTime = eoIdAndTime;
    }
}
