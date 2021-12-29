package tarzan.material.domain.vo;

import java.io.Serializable;

/**
 * @program: MtFrame
 * @description: 前台维护用视图
 * @author: Mr.Zxl
 * @create: 2018-12-04 14:37
 **/
public class MaterialCategoryAssignVO implements Serializable {
    private static final long serialVersionUID = -1143635772670926438L;

    private String materialCategoryAssignId;
    private String materialSiteId;
    private String materialId;
    private String siteId;
    private String siteCode;
    private String siteName;
    private String siteType;
    private String typeDesc;
    private String materialCategorySetId;
    private String materialCategoryId;
    private String categorySetCode;
    private String categorySetDesc;
    private String categoryCode;
    private String categoryDesc;
    private String enableFlag;


    public String getMaterialCategoryAssignId() { return materialCategoryAssignId; }

    public void setMaterialCategoryAssignId(String materialCategoryAssignId) { this.materialCategoryAssignId = materialCategoryAssignId; }

    public String getMaterialSiteId() { return materialSiteId; }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    public String getMaterialCategoryId() { return materialCategoryId; }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
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

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }


    public String getMaterialCategorySetId() {
        return materialCategorySetId;
    }

    public void setMaterialCategorySetId(String materialCategorySetId) {
        this.materialCategorySetId = materialCategorySetId;
    }

    public String getCategorySetCode() {
        return categorySetCode;
    }

    public void setCategorySetCode(String categorySetCode) {
        this.categorySetCode = categorySetCode;
    }

    public String getCategorySetDesc() {
        return categorySetDesc;
    }

    public void setCategorySetDesc(String categorySetDesc) {
        this.categorySetDesc = categorySetDesc;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
