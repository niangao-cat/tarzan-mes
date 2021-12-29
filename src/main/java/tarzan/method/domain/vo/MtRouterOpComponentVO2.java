package tarzan.method.domain.vo;

import java.io.Serializable;

public class MtRouterOpComponentVO2 implements Serializable {
    private static final long serialVersionUID = -4085617686701162522L;

    private String materialId; // 物料
    private String routerId; // 工艺路线

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }
}
