package tarzan.material.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: chuang.yang
 * @Date: 2020/7/13 10:22
 * @Description:
 */
public class MtMaterialSiteVO6 implements Serializable {
    private static final long serialVersionUID = 8033251934063999954L;

    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("站点ID集合")
    private List<String> siteIds;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public List<String> getSiteIds() {
        return siteIds;
    }

    public void setSiteIds(List<String> siteIds) {
        this.siteIds = siteIds;
    }
}
