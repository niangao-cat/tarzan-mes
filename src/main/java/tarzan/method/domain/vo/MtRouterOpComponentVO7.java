package tarzan.method.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: chuang.yang
 * @Date: 2020/11/2 20:34
 * @Description:
 */
public class MtRouterOpComponentVO7 implements Serializable {
    private static final long serialVersionUID = 464799212691286738L;

    @ApiModelProperty("装配清单ID")
    private String bomId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("装配清单类型")
    private String componentType;

    public MtRouterOpComponentVO7() {}

    public MtRouterOpComponentVO7(String bomId, String materialId, String componentType) {
        this.bomId = bomId;
        this.materialId = materialId;
        this.componentType = componentType;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtRouterOpComponentVO7 that = (MtRouterOpComponentVO7) o;
        return Objects.equals(bomId, that.bomId) && Objects.equals(materialId, that.materialId)
                        && Objects.equals(componentType, that.componentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bomId, materialId, componentType);
    }
}
