package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class MtBomSiteAssignVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3399944739714619882L;
    
    @ApiModelProperty(value = "主键")
    private String bomId;
    
    @ApiModelProperty(value = "分配表主键（新增无值）")
    private String assignId;
    
    @ApiModelProperty(value = "站点主键")
    private String siteId;
    
    @ApiModelProperty(value = "站点编码")
    private String siteCode;
    
    @ApiModelProperty(value = "站点类型")
    private String siteType;
    
    @ApiModelProperty(value = "站点类型描述")
    private String siteTypeDesc;
    
    @ApiModelProperty(value = "站点名称 ")
    private String siteName;
    
    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
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

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public String getSiteTypeDesc() {
        return siteTypeDesc;
    }

    public void setSiteTypeDesc(String siteTypeDesc) {
        this.siteTypeDesc = siteTypeDesc;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

}
