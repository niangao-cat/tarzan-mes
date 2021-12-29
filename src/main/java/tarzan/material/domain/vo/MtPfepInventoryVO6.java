package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import tarzan.material.domain.entity.MtPfepInventory;

/**
 * @author Leeloing
 * @date 2019-09-18 10:42
 */
public class MtPfepInventoryVO6 extends MtPfepInventory implements Serializable {

    private static final long serialVersionUID = 2990366865713414402L;

    @ApiModelProperty(value = "物料ID")
    private String materialId;
    @ApiModelProperty(value = "站点ID")
    private String siteId;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
