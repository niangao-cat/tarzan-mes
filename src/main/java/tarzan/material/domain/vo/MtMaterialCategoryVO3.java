package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtMaterialCategoryVO3 implements Serializable {

    private static final long serialVersionUID = -6542554820715703907L;

    @ApiModelProperty(value = "物料类别Id")
    private String materialCategoryId;
    
    @ApiModelProperty(value = "物料类别编码")
    private String categoryCode;

    @ApiModelProperty(value = "物料类别描述")
    private String description;

    @ApiModelProperty(value = "物料类别集Id")
    private String materialCategorySetId;
    
    @ApiModelProperty(value = "物料类别集编码")
    private String CategorySetCode;
    
    @ApiModelProperty(value = "物料类别集编码")
    private String CategorySetDesc;
    
    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
    }

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

    public String getCategorySetCode() {
        return CategorySetCode;
    }

    public void setCategorySetCode(String categorySetCode) {
        CategorySetCode = categorySetCode;
    }

    public String getCategorySetDesc() {
        return CategorySetDesc;
    }

    public void setCategorySetDesc(String categorySetDesc) {
        CategorySetDesc = categorySetDesc;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
    
}
