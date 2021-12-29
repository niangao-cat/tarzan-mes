package tarzan.material.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class MtMaterialCategorySiteDTO implements Serializable {
    private static final long serialVersionUID = -2167470854917406443L;

    @ApiModelProperty(value = "物料类别分配站点主键")
    private String materialCategorySiteId;
    
    @ApiModelProperty(value = "物料类别主键",required = true)
    @NotBlank
    private String materialCategoryId;
    
    @ApiModelProperty(value = "站点主键",required = true)
    @NotBlank
    private String siteId;

    public String getMaterialCategorySiteId() {
        return materialCategorySiteId;
    }

    public void setMaterialCategorySiteId(String materialCategorySiteId) {
        this.materialCategorySiteId = materialCategorySiteId;
    }

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
}
