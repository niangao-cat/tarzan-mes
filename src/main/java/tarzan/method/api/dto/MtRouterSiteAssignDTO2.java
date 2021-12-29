package tarzan.method.api.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtRouterSiteAssignDTO2 implements Serializable {
    private static final long serialVersionUID = -3445625153283307090L;

    @ApiModelProperty("工艺路线站点分配ID")
    private String routerSiteAssignId;
    @ApiModelProperty(value = "工艺路线ID")
    @NotBlank
    private String routerId;
    @ApiModelProperty(value = "站点ID")
    @NotBlank
    private String siteId;
    @ApiModelProperty(value = "站点编码")
    private String siteCode;
    @ApiModelProperty(value = "站点名称")
    private String siteName;
    @ApiModelProperty(value = "站点类型")
    private String siteType;
    @ApiModelProperty(value = "是否有效")
    @NotBlank
    private String enableFlag;

    public String getRouterSiteAssignId() {
        return routerSiteAssignId;
    }

    public void setRouterSiteAssignId(String routerSiteAssignId) {
        this.routerSiteAssignId = routerSiteAssignId;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
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

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
