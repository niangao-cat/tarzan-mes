package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtMaterialCategorySetVO
 * @description
 * @date 2019年09月16日 14:28
 */
public class MtMaterialCategorySetVO implements Serializable {

    private static final long serialVersionUID = -2452617707867939650L;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    private String materialCategorySetId;

    @ApiModelProperty(value = "物料类别集编码", required = true)
    private String categorySetCode;

    @ApiModelProperty(value = "物料类别集描述")
    private String description;

    @ApiModelProperty(value = "是否计划默认类别集")
    private String defaultScheduleFlag;

    @ApiModelProperty(value = "是否采购默认类别集")
    private String defaultPurchaseFlag;

    @ApiModelProperty(value = "是否生产默认类别集")
    private String defaultManufacturingFlag;

    @ApiModelProperty(value = "是否有效", required = true)
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

    public String getDefaultPurchaseFlag() {
        return defaultPurchaseFlag;
    }

    public void setDefaultPurchaseFlag(String defaultPurchaseFlag) {
        this.defaultPurchaseFlag = defaultPurchaseFlag;
    }

    public String getDefaultManufacturingFlag() {
        return defaultManufacturingFlag;
    }

    public void setDefaultManufacturingFlag(String defaultManufacturingFlag) {
        this.defaultManufacturingFlag = defaultManufacturingFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
