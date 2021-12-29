package tarzan.order.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年12月16日 下午2:45:33
 *
 */
public class MtWorkOrderVO47 implements Serializable {
    
    private static final long serialVersionUID = 7081868436606829731L;
    
    @ApiModelProperty(value = "生产指令ID",required = true)
    private String workOrderId;
    @ApiModelProperty(value = "组件编码")
    private String materialCode;
    @ApiModelProperty(value = "组件名称")
    private String materialName;
    @ApiModelProperty(value = "步骤编码")
    private String step;
    @ApiModelProperty(value = "步骤描述")
    private String stepDesc;
    @ApiModelProperty(value = "排序方向 DESC/ASC")
    private String sortDirection;

    //add by sanfeng.zhang for xuni 2020/10/01
    @ApiModelProperty(value = "装配方式")
    private String assembleMethodDesc;
    
    public String getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
    public String getMaterialCode() {
        return materialCode;
    }
    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }
    public String getMaterialName() {
        return materialName;
    }
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
    public String getStep() {
        return step;
    }
    public void setStep(String step) {
        this.step = step;
    }
    public String getStepDesc() {
        return stepDesc;
    }
    public void setStepDesc(String stepDesc) {
        this.stepDesc = stepDesc;
    }
    public String getSortDirection() {
        return sortDirection;
    }
    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getAssembleMethodDesc() {
        return assembleMethodDesc;
    }

    public void setAssembleMethodDesc(String assembleMethodDesc) {
        this.assembleMethodDesc = assembleMethodDesc;
    }
}
