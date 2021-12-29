package tarzan.general.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2019/12/13 10:17
 * @Author: ${yiyang.xie}
 */
public class MtUserOrganizationDTO6 implements Serializable {
    private static final long serialVersionUID = -1504941343204601302L;

    @ApiModelProperty(value = "站点Id")
    private String siteId;

    @ApiModelProperty(value = "站点编码")
    private String siteCode;

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
}
