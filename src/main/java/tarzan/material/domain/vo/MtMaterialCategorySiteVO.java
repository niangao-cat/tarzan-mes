package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年8月21日 下午2:42:09
 *
 */
public class MtMaterialCategorySiteVO implements Serializable {

    private static final long serialVersionUID = -7411273082026019267L;
    
    @ApiModelProperty(value = "物料类别分配站点主键")
    private String materialCategorySiteId;
    
    @ApiModelProperty(value = "站点主键")
    private String siteId;
    
    @ApiModelProperty(value = "站点编码")
    private String siteCode;
    
    @ApiModelProperty(value = "站点名称")
    private String siteName;
    
    @ApiModelProperty(value = "站点类型")
    private String siteType;
    
    @ApiModelProperty(value = "站点类型描述")
    private String siteTypeDesc;
    
    @ApiModelProperty(value = "启用状态")
    private String enableFlag;

    public String getMaterialCategorySiteId() {
        return materialCategorySiteId;
    }

    public void setMaterialCategorySiteId(String materialCategorySiteId) {
        this.materialCategorySiteId = materialCategorySiteId;
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

    public String getSiteTypeDesc() {
        return siteTypeDesc;
    }

    public void setSiteTypeDesc(String siteTypeDesc) {
        this.siteTypeDesc = siteTypeDesc;
    }
    
}