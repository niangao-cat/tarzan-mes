package tarzan.modeling.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 根据组织查询货位传入参数使用DTO
 * 
 * @author benjamin
 */
public class MtModOrganizationRelDTO6 implements Serializable {
    private static final long serialVersionUID = 5571949353920676277L;
    @ApiModelProperty(value = "站点Id")
    private String siteId;
    @ApiModelProperty(value = "货位编码")
    private String locatorCode;
    @ApiModelProperty(value = "货位名称")
    private String locatorName;

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
    }

    public String getLocatorName() {
        return locatorName;
    }

    public void setLocatorName(String locatorName) {
        this.locatorName = locatorName;
    }
}
