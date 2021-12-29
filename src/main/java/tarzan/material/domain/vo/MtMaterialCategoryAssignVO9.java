package tarzan.material.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: songren.yang
 * @Date: 2020/8/20
 * @Description:
 */
public class MtMaterialCategoryAssignVO9 implements Serializable {
    private static final long serialVersionUID = 4109736527864593102L;

    @ApiModelProperty(value = "物料Id")
    private String materialId;

    @ApiModelProperty("物料类别Id")
    private String materialCategoryId;

    @ApiModelProperty("站点Id")
    private String siteId;


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

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }
}


