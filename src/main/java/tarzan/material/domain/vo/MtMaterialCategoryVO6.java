package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author {yiyang.xie@hand-china.com}
 */
public class MtMaterialCategoryVO6 implements Serializable {

    private static final long serialVersionUID = 3538904906994568239L;
    @ApiModelProperty("工艺路线ID")
    private String materialCategoryId;
    @ApiModelProperty("工艺路线名称")
    private String categoryCode;
    @ApiModelProperty("工艺路线类型")
    private String description;
    @ApiModelProperty("工艺路线版本")
    private String materialCategorySetId;
    @ApiModelProperty("工艺路线描述")
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
