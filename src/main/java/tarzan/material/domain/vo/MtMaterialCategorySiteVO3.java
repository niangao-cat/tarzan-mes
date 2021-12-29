package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年8月21日 下午2:42:09
 *
 */
public class MtMaterialCategorySiteVO3 implements Serializable {

    private static final long serialVersionUID = -4356459056432034278L;

    @ApiModelProperty(value = "站点编码")
    private String siteCode;
    
    @ApiModelProperty(value = "站点名称")
    private String siteName;
    
    @ApiModelProperty(value = "站点类型（描述）")
    private String siteTypeDesc;
    
    @ApiModelProperty(value = "物料类别主键",required = true)
    private String materialCategoryId;

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

    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
    }

    public String getSiteTypeDesc() {
        return siteTypeDesc;
    }

    public void setSiteTypeDesc(String siteTypeDesc) {
        this.siteTypeDesc = siteTypeDesc;
    }
    
}