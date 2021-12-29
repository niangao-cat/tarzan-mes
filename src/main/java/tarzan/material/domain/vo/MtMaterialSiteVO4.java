package tarzan.material.domain.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: chuang.yang
 * @Date: 2020/4/7 11:37
 * @Description:
 */
public class MtMaterialSiteVO4 implements Serializable {
    private static final long serialVersionUID = -4255961981093261253L;

    @ApiModelProperty("主键，标识唯一一条记录")
    private String materialSiteId;

    @ApiModelProperty(value = "物料，来自物料表，对应唯一物料")
    private String materialId;

    @ApiModelProperty(value = "站点，来自站点表，对应唯一站点")
    private String siteId;

    @ApiModelProperty(value = "外部来源属性ID")
    private String sourceIdentificationId;

    @ApiModelProperty(value = "启用状态")
    private String enableFlag;

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

    public String getSourceIdentificationId() {
        return sourceIdentificationId;
    }

    public void setSourceIdentificationId(String sourceIdentificationId) {
        this.sourceIdentificationId = sourceIdentificationId;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}


