package tarzan.material.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description:
 * @Date: 2020/4/8 10:14
 * @Author: $yiyang.xie
 */
public class MtMaterialCategoryAssignVO3 implements Serializable {
    private static final long serialVersionUID = 6888692017063630390L;

    @ApiModelProperty("站点Id")
    private String siteId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("物料类别集ID")
    private String materialCategorySetId;


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

    public String getMaterialCategorySetId() {
        return materialCategorySetId;
    }

    public void setMaterialCategorySetId(String materialCategorySetId) {
        this.materialCategorySetId = materialCategorySetId;
    }
}


