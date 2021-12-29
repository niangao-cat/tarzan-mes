package tarzan.modeling.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtModSiteDTO5 implements Serializable {
    private static final long serialVersionUID = 1151697786030930347L;

    @ApiModelProperty("用户Id")
    private Long userId;
    @ApiModelProperty("站点类型")
    private String siteType;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }
}
