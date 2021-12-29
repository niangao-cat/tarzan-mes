package tarzan.actual.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2020/6/15 10:43
 * @Description:
 */
public class MtEoComponentActualVO28 implements Serializable {
    private static final long serialVersionUID = 7704181143286404969L;

    @ApiModelProperty("执行作业")
    private String eoId;
    @ApiModelProperty("物料")
    private String materialId;
    @ApiModelProperty("组件类型")
    private String componentType;
    @ApiModelProperty("工艺")
    private String operationId;
    @ApiModelProperty("装配时装配清单ID")
    private String bomId;

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }
}
