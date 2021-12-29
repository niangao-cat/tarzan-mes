package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年12月16日 下午2:45:33
 *
 */
public class MtWorkOrderVO43 implements Serializable {
    
    private static final long serialVersionUID = -5550193318012035837L;
    
    @ApiModelProperty(value = "生产指令ID",required = true)
    private String workOrderId;
    @ApiModelProperty(value = "组件ID",required = true)
    private String bomComponentId;
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    
    public String getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
    public String getBomComponentId() {
        return bomComponentId;
    }
    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }
    public String getMaterialId() {
        return materialId;
    }
    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

}
