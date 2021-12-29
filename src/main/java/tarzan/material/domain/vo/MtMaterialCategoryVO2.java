package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtMaterialCategoryVO2 implements Serializable {

    private static final long serialVersionUID = 8394974222677364241L;

    @ApiModelProperty(value = "物料类别编码")
    private String categoryCode;

    @ApiModelProperty(value = "物料类别描述")
    private String description;

    @ApiModelProperty(value = "物料类别集编码")
    private String materialCategorySetId;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterialCategorySetId() {
        return materialCategorySetId;
    }

    public void setMaterialCategorySetId(String materialCategorySetId) {
        this.materialCategorySetId = materialCategorySetId;
    }
    
}
