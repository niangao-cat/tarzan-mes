package tarzan.actual.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xiao tang
 * @date 2020-02-12 18:07
 */
public class MtProcessWorkingVO5 implements Serializable {
    
    private static final long serialVersionUID = 3174059217416767051L;

    @ApiModelProperty(value = "调度数量", required = true)
    private Double assignQty;
    
    @ApiModelProperty(value = "工作单元编码", required = true)
    private String workcellCode;
    
    @ApiModelProperty(value = "优先级", required = true)
    private Long priority;
    
    @ApiModelProperty(value = "调度结果主键", required = true)
    private String eoDispatchActionId;

    public Double getAssignQty() {
        return assignQty;
    }

    public void setAssignQty(Double assignQty) {
        this.assignQty = assignQty;
    }

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getEoDispatchActionId() {
        return eoDispatchActionId;
    }

    public void setEoDispatchActionId(String eoDispatchActionId) {
        this.eoDispatchActionId = eoDispatchActionId;
    }

}
