package tarzan.material.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2020/4/7 15:29
 * @Description:
 */
public class MtMaterialCategorySiteVO7 implements Serializable {
    private static final long serialVersionUID = 12235413470094933L;

    @ApiModelProperty(value = "物料类别分配站点主键")
    private String materialCategorySiteId;

    @ApiModelProperty("物料类别Id")
    private String materialCategoryId;

    @ApiModelProperty("站点Id")
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


