package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年12月20日 下午6:38:45
 *
 */
public class MtWorkOrderVO52 implements Serializable {
    
    private static final long serialVersionUID = -3963008847687022725L;
    
    @ApiModelProperty(value = "生产指令ID",required = true)
    private String workOrderId;
    @ApiModelProperty(value = "操作类型",required = true)
    private String operationType;
    
    public String getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
    public String getOperationType() {
        return operationType;
    }
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
    
}
