package tarzan.material.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description:
 * @Date: 2020/4/8 13:43
 * @Author: $yiyang.xie
 */
public class MtMaterialCategoryAssignVO5 implements Serializable {
    private static final long serialVersionUID = 8990774943271626196L;

    @ApiModelProperty("站点Id")
    private String siteId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("默认类型")
    private String defaultType;

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
}


