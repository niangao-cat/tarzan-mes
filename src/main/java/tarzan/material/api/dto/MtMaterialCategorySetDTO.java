package tarzan.material.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author YUAN.YUAN
 */
public class MtMaterialCategorySetDTO implements Serializable {
    private static final long serialVersionUID = 4254294353866466453L;

    public static final String FIELD_CATEGORY_SET_CODE = "categorySetCode";
    public static final String FIELD_DESCRIPTION = "description";


    @ApiModelProperty(value = "物料类别集编码")
    private String categorySetCode;
    @ApiModelProperty(value = "物料类别集描述")
    private String description;

    /**
     * @return 物料类别集编码
     */
    public String getCategorySetCode() {
        return categorySetCode;
    }

    public void setCategorySetCode(String categorySetCode) {
        this.categorySetCode = categorySetCode;
    }

    /**
     * @return 物料类别集描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
