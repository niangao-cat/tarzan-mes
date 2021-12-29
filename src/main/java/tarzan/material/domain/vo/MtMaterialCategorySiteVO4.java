package tarzan.material.domain.vo;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtMaterialCategorySiteVO4 implements Serializable {
    private static final long serialVersionUID = 2209243925609742486L;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtMaterialCategorySiteVO4 that = (MtMaterialCategorySiteVO4) o;
        return Objects.equals(getMaterialCategoryId(), that.getMaterialCategoryId())
                && Objects.equals(getSiteId(), that.getSiteId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaterialCategoryId(), getSiteId());
    }


}
