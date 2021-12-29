package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2020/1/7 16:10
 * @Author: ${yiyang.xie}
 */
public class MtEoStepWipDTO implements Serializable {
    private static final long serialVersionUID = 4574194362348751893L;

    @ApiModelProperty("执行作业编码")
    private String eoNum;
    @ApiModelProperty(value = "工艺路线ID")
    private String routerId;
    @ApiModelProperty(value = "步骤识别码")
    private String stepName;
    @ApiModelProperty(value = "工作单元编码")
    private String workcellCode;
    @ApiModelProperty("工艺ID")
    private String operationId;

    public String getEoNum() {
        return eoNum;
    }

    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}