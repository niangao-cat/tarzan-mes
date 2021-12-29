package tarzan.material.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import tarzan.material.domain.entity.MtPfepInventoryCategory;

/**
 * description
 *
 * @author HAND-MC 2019/08/16 15:12
 */
public class MtPfepInventoryCategoryDTO extends MtPfepInventoryCategory implements Serializable {

    private static final long serialVersionUID = -4573253597422435361L;

    @ApiModelProperty(value = "物料类别ID",required = true)
    private String materialCategoryId;
    @ApiModelProperty(value = "站点ID",required = true)
    private String siteId;
    @ApiModelProperty("扩展属性集合")
    private List<MtExtendAttrDTO3> mtPfepInventoryCategoryAttrs;

    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public List<MtExtendAttrDTO3> getMtPfepInventoryCategoryAttrs() {
        return mtPfepInventoryCategoryAttrs;
    }

    public void setMtPfepInventoryCategoryAttrs(List<MtExtendAttrDTO3> mtPfepInventoryCategoryAttrs) {
        this.mtPfepInventoryCategoryAttrs = mtPfepInventoryCategoryAttrs;
    }

}
