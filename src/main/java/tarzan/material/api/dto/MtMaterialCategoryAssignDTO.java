package tarzan.material.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author YUAN.YUAN
 */
public class MtMaterialCategoryAssignDTO implements Serializable {
    private static final long serialVersionUID = 2950517714849467104L;

    @ApiModelProperty(value = "主键ID,标识唯一一条记录", required = true)
    private String materialCategoryAssignId;
    @ApiModelProperty(value = "物料站点主键，标识唯一物料站点对应关系", required = true)
    private String materialSiteId;
    @ApiModelProperty(value = "物料类别", required = true)
    private String materialCategoryId;
    @ApiModelProperty(value = "站点id", required = true)
    private String siteId;

    /**
     * @return 主键ID,标识唯一一条记录
     */
    public String getMaterialCategoryAssignId() {
        return materialCategoryAssignId;
    }

    public void setMaterialCategoryAssignId(String materialCategoryAssignId) {
        this.materialCategoryAssignId = materialCategoryAssignId;
    }

    /**
     * @return 物料站点主键，标识唯一物料站点对应关系
     */
    public String getMaterialSiteId() {
        return materialSiteId;
    }

    public void setMaterialSiteId(String materialSiteId) {
        this.materialSiteId = materialSiteId;
    }

    /**
     * @return 物料类别
     */
    public String getMaterialCategoryId() {
        return materialCategoryId;
    }

    public void setMaterialCategoryId(String materialCategoryId) {
        this.materialCategoryId = materialCategoryId;
    }

    /**
     * @return 站点id
     */
    public String getSiteId() { return siteId; }
    public void setSiteId(String siteId) { this.siteId = siteId; }
}
