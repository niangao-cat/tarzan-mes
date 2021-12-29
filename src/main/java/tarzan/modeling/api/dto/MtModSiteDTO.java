package tarzan.modeling.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModSiteDTO implements Serializable {
    private static final long serialVersionUID = -1771157560969962381L;

    @ApiModelProperty("站点ID，主键唯一标识")
    private String siteId;
    @ApiModelProperty(value = "站点编码", required = true)
    private String siteCode;
    @ApiModelProperty(value = "站点名称", required = true)
    private String siteName;
    @ApiModelProperty(value = "站点类型", required = true)
    private String siteType;
    @ApiModelProperty(value = "有效性", required = true)
    private String enableFlag;

    /**
     * @return 站点ID，主键唯一标识
     */
    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return 站点编码
     */
    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    /**
     * @return 站点名称
     */
    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    /**
     * @return 站点类型
     */
    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    /**
     * @return 有效性
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

}
