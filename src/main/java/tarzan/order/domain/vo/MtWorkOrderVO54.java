package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年12月20日 下午6:38:45
 *
 */
public class MtWorkOrderVO54 implements Serializable {
    
    private static final long serialVersionUID = 1700439436345150375L;
    
    @ApiModelProperty(value = "生产指令ID")
    private String workOrderId;
    @ApiModelProperty(value = "工单编码")
    private String workOrderNum;
    @ApiModelProperty(value = "关系")
    private List<String> rel;
    @ApiModelProperty(value = "关系类型")
    private List<String> relType;
    
    public String getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
    public String getWorkOrderNum() {
        return workOrderNum;
    }
    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
    }
    public List<String> getRel() {
        return rel;
    }
    public void setRel(List<String> rel) {
        this.rel = rel;
    }
    public List<String> getRelType() {
        return relType;
    }
    public void setRelType(List<String> relType) {
        this.relType = relType;
    }
    
}
