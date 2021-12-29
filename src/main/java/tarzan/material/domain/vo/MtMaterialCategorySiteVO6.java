package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author {yiyang.xie@hand-china.com}
 */
public class MtMaterialCategorySiteVO6 implements Serializable {

    private static final long serialVersionUID = -5146920989434716540L;
    @ApiModelProperty("工艺路线ID")
    private String materialCategorySetId;
    @ApiModelProperty("工艺路线名称")
    private String categorySetCode;
    @ApiModelProperty("工艺路线类型")
    private String description;
    @ApiModelProperty("工艺路线版本")
    private String defaultScheduleFlag;
    @ApiModelProperty("工艺路线描述")
    private String defaultManufacturingFlag;
    @ApiModelProperty("是否为当前版本")
    private String defaultPurchaseFlag;
    @ApiModelProperty("工艺路线状态")
    private String enableFlag;

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

    public String getDefaultScheduleFlag() {
        return defaultScheduleFlag;
    }

    public void setDefaultScheduleFlag(String defaultScheduleFlag) {
        this.defaultScheduleFlag = defaultScheduleFlag;
    }

    public String getDefaultManufacturingFlag() {
        return defaultManufacturingFlag;
    }

    public void setDefaultManufacturingFlag(String defaultManufacturingFlag) {
        this.defaultManufacturingFlag = defaultManufacturingFlag;
    }

    public String getDefaultPurchaseFlag() {
        return defaultPurchaseFlag;
    }

    public void setDefaultPurchaseFlag(String defaultPurchaseFlag) {
        this.defaultPurchaseFlag = defaultPurchaseFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

}
