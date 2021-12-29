package tarzan.modeling.api.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModSiteDTO2 implements Serializable {
    private static final long serialVersionUID = -1771157560969962381L;

    @ApiModelProperty(value = "站点编码")
    private String siteCode;

    @ApiModelProperty(value = "站点描述")
    private String siteName;

    @ApiModelProperty(value = "站点类型")
    private String siteType;

    @ApiModelProperty(value = "启用状态")
    private String enableFlag;

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
