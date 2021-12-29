package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/10/9 10:50
 * @Author: ${yiyang.xie}
 */
public class MtAssembleConfirmActualVO19 implements Serializable {
    
    private static final long serialVersionUID = 197205162124274474L;
    
    @ApiModelProperty(value = "执行作业ID列表")
    private List<String> eoIdList;
    @ApiModelProperty(value = "工艺ID")
    private String operationId;
    
    public List<String> getEoIdList() {
        return eoIdList;
    }
    public void setEoIdList(List<String> eoIdList) {
        this.eoIdList = eoIdList;
    }
    public String getOperationId() {
        return operationId;
    }
    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
    
}
