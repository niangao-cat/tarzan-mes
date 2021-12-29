package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtMaterialSiteVO
 * @description
 * @date 2019年09月17日 11:00
 */
public class MtMaterialSiteVO implements Serializable {
    private static final long serialVersionUID = -7823385886443002431L;

    @ApiModelProperty("主键，标识唯一一条记录")
    private String materialSiteId;

    @ApiModelProperty(value = "物料，来自物料表，对应唯一物料", required = true)
    private String materialId;

    @ApiModelProperty(value = "站点，来自站点表，对应唯一站点", required = true)
    private String siteId;

    private String enableFlag;

    private String sourceIdentificationId;

    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
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

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getSourceIdentificationId() {
        return sourceIdentificationId;
    }

    public void setSourceIdentificationId(String sourceIdentificationId) {
        this.sourceIdentificationId = sourceIdentificationId;
    }

}
