package tarzan.material.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description:
 * @Date: 2020/4/8 13:45
 * @Author: $yiyang.xie
 */
public class MtMaterialCategoryAssignVO6 implements Serializable {
    private static final long serialVersionUID = 8607809621294443921L;

    @ApiModelProperty("站点Id")
    private String siteId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("默认类型")
    private String defaultType;
    @ApiModelProperty("物料类别ID")
    private String materialCategoryId;

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

    public String getDefaultType() {
        return defaultType;
    }

    public void setDefaultType(String defaultType) {
        this.defaultType = defaultType;
    }

    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
    }
}


