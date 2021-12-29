package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author {yiyang.xie@hand-china.com}
 */
public class MtSubstepVO1 implements Serializable {

    private static final long serialVersionUID = -1948829466529617154L;
    @ApiModelProperty("物料ID")
    private String substepId;
    @ApiModelProperty("物料编码")
    private String siteId;
    @ApiModelProperty("物料名称")
    private String siteCode;
    @ApiModelProperty("物料图号")
    private String siteName;
    @ApiModelProperty("物料简码")
    private String substepName;
    @ApiModelProperty("材质&型号")
    private String substepType;
    @ApiModelProperty("物料长度值")
    private String description;
    @ApiModelProperty("物料宽度值")
    private String longDescription;

    public String getSubstepId() {
        return substepId;
    }

    public void setSubstepId(String substepId) {
        this.substepId = substepId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSubstepName() {
        return substepName;
    }

    public void setSubstepName(String substepName) {
        this.substepName = substepName;
    }

    public String getSubstepType() {
        return substepType;
    }

    public void setSubstepType(String substepType) {
        this.substepType = substepType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDeccription) {
        this.longDescription = longDeccription;
    }

}
