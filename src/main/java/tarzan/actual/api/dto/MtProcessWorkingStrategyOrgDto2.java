package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 2020-02-06
 */
public class MtProcessWorkingStrategyOrgDto2 implements Serializable {
    private static final long serialVersionUID = -7096665780064761003L;

    @ApiModelProperty("工序作业平台配置唯一标识")
    private String strategyId;

    @ApiModelProperty("顶层站点ID")
    private String topSiteId;

    @ApiModelProperty(value = "组织ID")
    private String proWorkingOrganizationId;

    @ApiModelProperty(value = "组织类型")
    private String organizationType;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public String getTopSiteId() {
        return topSiteId;
    }

    public void setTopSiteId(String topSiteId) {
        this.topSiteId = topSiteId;
    }

    public String getProWorkingOrganizationId() {
        return proWorkingOrganizationId;
    }

    public void setProWorkingOrganizationId(String proWorkingOrganizationId) {
        this.proWorkingOrganizationId = proWorkingOrganizationId;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
