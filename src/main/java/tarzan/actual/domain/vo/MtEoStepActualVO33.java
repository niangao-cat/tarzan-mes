package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtEoStepActualVO33
 * @description
 * @date 2019年12月04日 14:26
 */
public class MtEoStepActualVO33 implements Serializable {
    private static final long serialVersionUID = -1937769689238968203L;

    @ApiModelProperty(value = "执行作业工艺路线实绩")
    private String eoRouterActualId;
    @ApiModelProperty(value = "执行作业步骤实绩")
    private String eoStepActualId;
    @ApiModelProperty(value = "工艺ID")
    private String operationId;

    // 增加参数
    @ApiModelProperty(value = "工艺描述")
    private String operation;

    // 初始化集合，避免空指针异常
    // private List<MtEoStepWorkcellActualHisVO2> eoStepWorkcellActualHisList = new ArrayList<>();

    private List<MtEoStepWorkcellActualVO12> eoStepWorkcellActualList = new ArrayList<>();

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getEoStepActualId() {
        return eoStepActualId;
    }

    public void setEoStepActualId(String eoStepActualId) {
        this.eoStepActualId = eoStepActualId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public List<MtEoStepWorkcellActualVO12> getEoStepWorkcellActualList() {
        return eoStepWorkcellActualList;
    }

    public void setEoStepWorkcellActualList(List<MtEoStepWorkcellActualVO12> eoStepWorkcellActualList) {
        this.eoStepWorkcellActualList = eoStepWorkcellActualList;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
