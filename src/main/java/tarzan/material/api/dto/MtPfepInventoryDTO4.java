package tarzan.material.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * description
 *
 * @author HAND-MC 2019/08/19 20:31
 */
public class MtPfepInventoryDTO4 implements Serializable {

    private static final long serialVersionUID = -9007958486807966340L;
    @ApiModelProperty(value = "主键ID")
    private String kid;
    @ApiModelProperty(value = "pfep类型(material/category)")
    private String type;
    private String materialId;
    private String categoryId;
    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
