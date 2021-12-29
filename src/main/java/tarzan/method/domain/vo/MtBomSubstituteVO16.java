package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/4/7 3:35 下午
 */
public class MtBomSubstituteVO16 implements Serializable {
    private static final long serialVersionUID = 529397120788873856L;
    @ApiModelProperty("组件物料ID")
    private String materialId;
    @ApiModelProperty("优先级")
    private Double priority;
    @ApiModelProperty("组件用量")
    private Double componentQty;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public Double getPriority() {
        return priority;
    }

    public void setPriority(Double priority) {
        this.priority = priority;
    }

    public Double getComponentQty() {
        return componentQty;
    }

    public void setComponentQty(Double componentQty) {
        this.componentQty = componentQty;
    }

}
