package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/7/14 10:13
 * @Description:
 */
public class MtBomSiteAssignVO2 implements Serializable {
    private static final long serialVersionUID = -3058438776185153554L;

    @ApiModelProperty("分配主键Id")
    private String assignId;
    @ApiModelProperty("装配清单ID")
    private String bomId;
    @ApiModelProperty("站点ID")
    private String siteId;
    @ApiModelProperty("有效性")
    private String enableFlag;

    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public MtBomSiteAssignVO2() {}

    public MtBomSiteAssignVO2(String assignId, String bomId, String siteId, String enableFlag) {
        this.assignId = assignId;
        this.bomId = bomId;
        this.siteId = siteId;
        this.enableFlag = enableFlag;
    }
}
