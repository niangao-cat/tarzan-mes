package tarzan.material.domain.vo;

import java.io.Serializable;

/**
 * @program: MtFrame
 * @description: 前台维护用视图
 * @author: Mr.Zxl
 * @create: 2018-12-04 14:37
 **/
public class MaterialSiteVO implements Serializable {
    private static final long serialVersionUID = 8783402632041319548L;
    private String materialSiteId;
    private String siteId;
    private String siteCode;
    private String siteName;
    private String siteType;
    private String enableFlag;

    private String materialId;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
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
