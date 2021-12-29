package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtMaterialCategoryVO4
 * @description
 * @date 2019年09月16日 17:17
 */
public class MtMaterialCategoryVO4 implements Serializable {

    private static final long serialVersionUID = -8074578878459543229L;
    @ApiModelProperty(value = "物料类别Id")
    private String materialCategoryId;

    @ApiModelProperty(value = "物料类别编码")
    private String categoryCode;

    @ApiModelProperty(value = "物料类别描述")
    private String description;

    @ApiModelProperty(value = "物料类别集Id")
    private String materialCategorySetId;

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

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
