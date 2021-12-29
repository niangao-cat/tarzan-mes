package tarzan.modeling.domain.vo;

import java.io.Serializable;

public class MtModSiteVO2 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1036527446812509873L;
    private String siteId;
    private String siteCode; // 站点编号
    private String siteName; // 站点名称
    private String siteType; // 站点类型
    private String siteTypeDesc;
    private String enableFlag; // 是否有效

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

    public String getSiteTypeDesc() {
        return siteTypeDesc;
    }

    public void setSiteTypeDesc(String siteTypeDesc) {
        this.siteTypeDesc = siteTypeDesc;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

}
