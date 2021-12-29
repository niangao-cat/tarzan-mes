package tarzan.material.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: chuang.yang
 * @Date: 2020/4/7 11:32
 * @Description:
 */
public class MtMaterialSiteVO3 implements Serializable {
    private static final long serialVersionUID = 4605611989673006775L;

    @ApiModelProperty(value = "物料，来自物料表，对应唯一物料")
    private String materialId;

    @ApiModelProperty(value = "站点，来自站点表，对应唯一站点")
    private String siteId;

    public String getMaterialSiteId() {
        return materialId+":"+siteId;
    }

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

    public MtMaterialSiteVO3(String materialId, String siteId) {
        this.materialId = materialId;
        this.siteId = siteId;
    }

    public MtMaterialSiteVO3() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtMaterialSiteVO3 that = (MtMaterialSiteVO3) o;
        return Objects.equals(getMaterialId(), that.getMaterialId()) && Objects.equals(getSiteId(), that.getSiteId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMaterialId(), getSiteId());
    }
}


