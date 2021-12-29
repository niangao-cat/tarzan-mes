package tarzan.material.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class MtMaterialCategorySetVO2 implements Serializable {
    private static final long serialVersionUID = 167751875398707655L;

    @ApiModelProperty(value = "默认类别")
    private String defaultType;

    @ApiModelProperty("主键ID，标识唯一一条记录")
    private String materialCategorySetId;

    @ApiModelProperty(value = "物料类别集编码", required = true)
    private String categorySetCode;

    @ApiModelProperty(value = "物料类别集描述")
    private String description;

    public String getDefaultType() {
        return defaultType;
    }

    public void setDefaultType(String defaultType) {
        this.defaultType = defaultType;
    }

    public String getMaterialCategorySetId() {
        return materialCategorySetId;
    }

    public void setMaterialCategorySetId(String materialCategorySetId) {
        this.materialCategorySetId = materialCategorySetId;
    }

    public String getCategorySetCode() {
        return categorySetCode;
    }

    public void setCategorySetCode(String categorySetCode) {
        this.categorySetCode = categorySetCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MtMaterialCategorySetVO2() {}

    public MtMaterialCategorySetVO2(String defaultType) {
        this.defaultType = defaultType;
    }
}


