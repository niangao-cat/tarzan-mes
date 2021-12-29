package tarzan.material.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description:
 * @Date: 2020/4/8 10:34
 * @Author: $yiyang.xie
 */
public class MtMaterialCategoryAssignVO4 implements Serializable {
    private static final long serialVersionUID = 2233654747212555396L;

    @ApiModelProperty("物料Id")
    private String materialId;
    @ApiModelProperty("站点Id")
    private String siteId;
    @ApiModelProperty("物料类别集ID")
    private String materialCategorySetId;
    @ApiModelProperty("物料类别Id")
    private String materialCategoryId;


    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getMaterialCategorySetId() {
        return materialCategorySetId;
    }

    public void setMaterialCategorySetId(String materialCategorySetId) {
        this.materialCategorySetId = materialCategorySetId;
    }

    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
    }
}


